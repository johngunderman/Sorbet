package sorbet;

import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import sourceparser.SourceParser;

import log.Logger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

public class StepEventHandler {

	private SourceParser sourceParser;
	private Logger logger;
	private VirtualMachine vm;

	private Threads threads;

	public StepEventHandler(SourceParser sourceParser, Logger logger,
			VirtualMachine vm) {

		this.sourceParser = sourceParser;
		this.logger = logger;
		this.vm = vm;

		this.threads = new Threads();

		requestEvents();
	}

	private void requestEvents() {
		EventRequestManager erm = vm.eventRequestManager();

		for (ThreadReference ref : vm.allThreads()) {
			StepRequest request = erm.createStepRequest(ref,
					StepRequest.STEP_LINE, StepRequest.STEP_INTO);
			/* TODO: Change this to match what our command line params are */
			request.addClassFilter("client.*");
			request.enable();
		}
	}

	public void handle(Event event) {
		if (event instanceof StepEvent == false) {
			return;
		}

		StepEvent stepEvent = (StepEvent) event;

		Location location = stepEvent.location();

		for (ThreadReference thread : vm.allThreads()) {
			String threadName = thread.name();
			
			if (threadName.equals("main")) {
				int stackDepth;
				try {
					stackDepth = thread.frameCount();
				} catch (IncompatibleThreadStateException e) {
					// No stack variables available for this thread

					continue;
				}
				if (threads.containsKey(threadName) == false) {
					threads.put(threadName, new Steps());
				}
				Steps steps = threads.get(threadName);
				if (steps.size() < stackDepth) {
					// Stack has grown so make a new Step

					steps.push(new Step());
				} else if (steps.size() > stackDepth) {
					// Stack has shrunk so pop a Step

					steps.pop();
				}
				if (steps.size() > 0) {
					Step lastStep = steps.pop();
					Step newStep = new Step();

					newStep.knownVariables = lastStep.knownVariables;
					newStep.location = location;

					try {
						// Log new variables from the last step
						logNewVariables(thread, lastStep, newStep);

						// Log the changed variables from the last step
						logChangedVariables(thread, lastStep, newStep);

						// Log step location
						logLocation(newStep);

						// Log used variables and record their values
						logUsedVariables(thread, newStep);
					} catch (AbsentInformationException e) {
						// TODO autogenerated or whatever

						e.printStackTrace();
					}

					// Push the new step
					steps.push(newStep);
				}
			}
		}

		return;
	}

	private void logNewVariables(ThreadReference thread, Step lastStep,
			Step newStep) throws AbsentInformationException {

		List<String> usedVariables = sourceParser.getVariables(
				newStep.location.sourcePath(), newStep.location.lineNumber());

		if (usedVariables != null) {
			HashSet<String> usedVarSet = new HashSet<String>(usedVariables);
			for (String variable : usedVarSet) {
				if (newStep.knownVariables.add(variable)) {
					logger.logVarCreated(variable,
							getVariableType(thread, variable));
				}
			}
		}
	}

	private String getVariableType(ThreadReference thread, String variable) {
		String type = "UNKNOWN";

		LocalVariable lv = null;
		try {
			lv = thread.frame(0).visibleVariableByName(variable);
			if (lv != null) {
				type = lv.typeName();
			}
		} catch (AbsentInformationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IncompatibleThreadStateException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return type;
	}

	private void logChangedVariables(ThreadReference thread, Step lastStep,
			Step newStep) {
		for (String lastVariable : lastStep.variableValues.keySet()) {

			String lastValue = lastStep.variableValues.get(lastVariable);
			String currentValue = getValue(thread, newStep.location,
					lastVariable);

			if (lastValue.equals(currentValue) == false) {
				logger.logVarChanged(lastVariable, currentValue);
			}
		}
	}

	private String getValue(ThreadReference thread, Location location,
			String variableName) {
		StackFrame stackFrame;
		try {
			stackFrame = thread.frame(0);
		} catch (IncompatibleThreadStateException e1) {
			// TODO Auto-generated catch block

			return "null";
		}

		LocalVariable variable;
		try {
			variable = stackFrame.visibleVariableByName(variableName);
		} catch (AbsentInformationException e) {
			// TODO Auto-generated catch block

			return "null";
		}

		if (variable != null) {
			// The variable was on the stack

			return stackFrame.getValue(variable).toString();
		} else {
			// The variable was not on the stack

			Value value = null;

			ObjectReference objectReference = stackFrame.thisObject();
			if (objectReference != null) {
				ReferenceType referenceType = objectReference.referenceType();
				Field field = referenceType.fieldByName(variableName);
				value = objectReference.getValue(field);
			}

			// handle static fields
			if (value == null) {
				ReferenceType referenceType = location.declaringType();
				Field field = referenceType.fieldByName(variableName);
				if (field != null) {
					value = referenceType.getValue(field);
				}
			}

			if (value != null) {
				return value.toString();
			} else {
				return "null"; // TODO: This returns null for static fields
								// outside the current class as well...
			}
		}
	}

	private void logLocation(Step newStep) throws AbsentInformationException {
		logger.logLines(newStep.location.sourcePath(),
				newStep.location.lineNumber());
	}

	private void logUsedVariables(ThreadReference thread, Step newStep)
			throws AbsentInformationException {

		List<String> usedVariables = sourceParser.getVariables(
				newStep.location.sourcePath(), newStep.location.lineNumber());

		if (usedVariables != null) {
			for (String variable : usedVariables) {
				String value = getValue(thread, newStep.location, variable);

				newStep.variableValues.put(variable, value);

				logger.logVarUsed(variable);
			}
		}
	}
}
