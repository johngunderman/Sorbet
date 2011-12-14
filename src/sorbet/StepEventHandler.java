package sorbet;

import java.util.HashSet;
import java.util.LinkedList;
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

	public void handleEvent(Event event) {
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
					threads.put(threadName, new StepStack());
				}
				StepStack stepStack = threads.get(threadName);
				
				if (stepStack.size() < stackDepth) {
					// Stack has grown so make a new Step

					stepStack.push(new Steps());
				} else if (stepStack.size() > stackDepth) {
					// Stack has shrunk so pop a Step

					stepStack.pop();
				}
				
				Steps steps = stepStack.peek();
				
				if (steps != null) {

					Step newStep = new Step();
					newStep.location = location;
					
					try {
						if (steps.empty() == false) {
							Step lastStep = steps.pop();
							newStep.declaredVariables = lastStep.declaredVariables;
							
							if (sameLocation(newStep.location, lastStep.location) == false) {
								// Log step location
								logLocation(lastStep);
								
								// Log variable changes
								logVariables(thread, lastStep, newStep);
							} else {
								newStep.usedVariableValues = lastStep.usedVariableValues;
							}
						}
						
						// Save used variable values for comparison in the next step
						saveUsedVariables(thread, newStep);
						
					} catch (AbsentInformationException e) {
						// No information available, move on
					}

					// Push the new step
					steps.push(newStep);
				}
			}
		}

		return;
	}

	private void logVariables(ThreadReference thread, Step lastStep, Step newStep) throws AbsentInformationException {
		
		Set<String> usedVariableNames = lastStep.usedVariableValues.keySet();
		
		List<String> justDeclaredVariables = new LinkedList<String>();
		
		for (String variableName : usedVariableNames) {
			String fullVariableName = getFullVariableName(variableName, thread, lastStep);
			
			if (lastStep.declaredVariables.contains(fullVariableName) == false) {
				// Variable was just declared
				justDeclaredVariables.add(fullVariableName);
							
			} else {
				// Variable was already declared and is therefore only used
				
				logger.logVarUsed(fullVariableName);
			
				if (lastStep.usedVariableValues.get(fullVariableName).equals(getValue(thread, lastStep.location, fullVariableName)) == false) {
					// Variable changed
					
					logger.logVarChanged(fullVariableName, getValue(thread, lastStep.location, fullVariableName));
				}
			}
		}
		
		for (String justDeclaredVariable : justDeclaredVariables) {
			lastStep.declaredVariables.add(justDeclaredVariable);
			newStep.declaredVariables.add(justDeclaredVariable);
			
			// Log new variable
			logger.logVarCreated(justDeclaredVariable,
					getVariableType(thread, justDeclaredVariable, lastStep.location.declaringType()));
			// Log its value
			logger.logVarChanged(justDeclaredVariable, getValue(thread, lastStep.location, justDeclaredVariable));	
		}
	}

	private String getFullVariableName(String variable, ThreadReference thread,
			Step newStep) {
		String name = null;
		LocalVariable lv = null;
		Field f = null;
		try {
			lv = thread.frame(0).visibleVariableByName(variable);
			if (lv != null) {
				name = variable;
			}
		} catch (AbsentInformationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IncompatibleThreadStateException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		if (name == null) {
			if (variable.contains("this.")) {
				variable = variable.substring(5);
			}
			f = newStep.location.declaringType().fieldByName(variable);
			if (f != null) {
				name = newStep.location.declaringType().name() + "." + variable;
			} else name = variable;
		}
		
		return name;
	}

	private String getVariableType(ThreadReference thread, String variable, ReferenceType ref) {
		String type = "UNKNOWN";

		LocalVariable lv = null;
		Field f = null;
		try {
			lv = thread.frame(0).visibleVariableByName(variable);
			if (lv != null) {
				type = lv.typeName();
			}
			
			String varName = variable;
			// this may need to get fixed up a bit later, but it should work
			// takes care of our fully qualified fields.
			if (variable.indexOf('.') != -1) {
				varName = variable.substring(variable.lastIndexOf('.') + 1);
			}
			
			f = ref.fieldByName(varName);
			if (f != null && type.equals("UNKNOWN")) {
				type = f.typeName();
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
			
			String varName = variableName;
			// this may need to get fixed up a bit later, but it should work
			// takes care of our fully qualified fields.
			if (variableName.indexOf('.') != -1) {
				varName = variableName.substring(variableName.lastIndexOf('.') + 1);
			}

			Value value = null;

			ObjectReference objectReference = stackFrame.thisObject();
			if (objectReference != null) {
				ReferenceType referenceType = objectReference.referenceType();
				Field field = referenceType.fieldByName(varName);
				value = objectReference.getValue(field);
			}

			// handle static fields
			if (value == null) {
				ReferenceType referenceType = location.declaringType();
				Field field = referenceType.fieldByName(varName);
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

	private void saveUsedVariables(ThreadReference thread, Step newStep)
			throws AbsentInformationException {

		List<String> usedVariables = sourceParser.getVariables(
				newStep.location.sourcePath(), newStep.location.lineNumber());

		if (usedVariables != null) {
			for (String variable : usedVariables) {
				variable = getFullVariableName(variable, thread, newStep);
				
				String value = getValue(thread, newStep.location, variable);

				newStep.usedVariableValues.put(variable, value);
			}
		}
	}
	
	private boolean sameLocation(Location location1, Location location2) throws AbsentInformationException {
		return location1.sourceName().equals(location2.sourceName()) &&
			location1.lineNumber() == location2.lineNumber();
	}
}
