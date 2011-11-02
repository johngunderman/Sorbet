package sorbet;

import java.util.List;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import log.*;

public class EventHandler {	
	public static final String CLASS_NAME = "client.Main";	
	
	public static Logger logger = new PrintLogger();
	
	public static void requestEvents(VirtualMachine vm) {
		EventRequestManager erm = vm.eventRequestManager();
		ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
		classPrepareRequest.addClassFilter(CLASS_NAME);
		classPrepareRequest.enable();
		
		for (ThreadReference ref : vm.allThreads()) {
			StepRequest request = erm.createStepRequest(ref, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
			request.addClassFilter(CLASS_NAME);
			request.enable();
		}
	}
	
	/**
	 * 
	 * @param vm
	 * @param event
	 * @return an action code
	 *   -1 stop debug loop on return
	 *    0 ignore
	 */
	public static int handle(VirtualMachine vm, Event event) {		
		if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
			// exit
			return -1;
		} else if (event instanceof ClassPrepareEvent) {
			handleClassPrepareEvent(vm, (ClassPrepareEvent)event);
		} else if (event instanceof ModificationWatchpointEvent) {
			handleModificationWatchPointEvent(vm, (ModificationWatchpointEvent)event);
		} else if (event instanceof StepEvent) {
			handleStepEvent(vm, (StepEvent)event);
		}
		
		return 0;
	}	

	private static void handleClassPrepareEvent(VirtualMachine vm, ClassPrepareEvent event) {
		// watch field on loaded class
		ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
		ReferenceType refType = classPrepEvent.referenceType();
		addFieldWatch(vm, refType);
	}
	
	private static void addFieldWatch(VirtualMachine vm, ReferenceType refType) {
		EventRequestManager erm = vm.eventRequestManager();
		List<Field> fields = refType.allFields();
		for(Field field : fields) {
		ModificationWatchpointRequest modificationWatchpointRequest = erm
				.createModificationWatchpointRequest(field);
		modificationWatchpointRequest.setEnabled(true);
		}
	}
	
	private static void handleModificationWatchPointEvent(VirtualMachine vm, ModificationWatchpointEvent event) {
		// a Test.foo has changed
		ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent)event;
		
		try {
			logger.log(modEvent.location().sourcePath(), modEvent.location().lineNumber(), modEvent.location().method().name(), 
						modEvent.field().name(), modEvent.valueToBe());
		} catch (AbsentInformationException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot access current filename!");
			e.printStackTrace();
		}
	}
	
	private static ThreadsMap threads = new ThreadsMap();
	
	private static void handleStepEvent(VirtualMachine vm, StepEvent event) {		
		Location location = event.location();
		
		String sourcePath;
		int lineNumber;
		String methodName; 
		
		try {
			sourcePath = location.sourcePath();
			lineNumber = location.lineNumber();
			methodName = location.method().name();
		} catch (AbsentInformationException e) {
			// No location information available for this step
			
			return;
		}
			
		for (ThreadReference thread : vm.allThreads()) {	
			String threadName = thread.name();
			
			if (threads.containsKey(threadName) == false) {
				VariablesStack variablesStack = new VariablesStack();
				variablesStack.setLineNumber(lineNumber);
				
				threads.put(threadName, variablesStack);
			}
			
			int frameCount;
			
			try {
				frameCount = thread.frameCount();
			} catch (IncompatibleThreadStateException e) {
				// No stack variables available for this thread
				
				continue;
			}
			
			VariablesStack variablesStack = threads.get(threadName);
			
			if (variablesStack.size() < frameCount) {
				// Stack has grown so make a new VariablesMap
				
				variablesStack.push(new VariablesMap());
			} else if (variablesStack.size() > frameCount) {
				// Stack has shrunk so pop a VariablesMap
				
				variablesStack.pop();
			}
			
			if (frameCount > 0) {				
				VariablesMap variablesMap = variablesStack.peek();
				
				try {
					StackFrame frame = thread.frame(0);
					
					for (LocalVariable variable : frame.visibleVariables()) {
						if (variablesMap.containsKey(variable.name())) {
							// Variable already existed
							
							Value oldValue = variablesMap.get(variable.name());
							Value newValue = frame.getValue(variable);
							
							// TODO: I think that this equals needs to be refined
							if (oldValue.equals(newValue) == false) {
								variablesMap.put(variable.name(), newValue);
								
								logger.log(sourcePath, variablesStack.getLineNumber(), methodName, variable.name(), newValue);
							}
						} else {
							// Variable was just declared
							
							Value newValue = frame.getValue(variable);
							
							variablesMap.put(variable.name(), newValue);
							
							logger.log(sourcePath, variablesStack.getLineNumber(), methodName, variable.name(), newValue);
						}
					}
					
					variablesStack.setLineNumber(lineNumber);
				} catch (IncompatibleThreadStateException e) {
					// No stack variables available for this thread
					
					continue;
				} catch (AbsentInformationException e) {
					// No stack variables available for this thread
					
					continue;
				} 
			}
		}
	}
}
