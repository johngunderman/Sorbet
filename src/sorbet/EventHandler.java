package sorbet;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

public abstract class EventHandler {	
	public static final String FIELD_NAME = "foo";
	public static final String CLASS_NAME = "client.Main";	
	
	public static void request(VirtualMachine vm) {
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
		Field field = refType.fieldByName(FIELD_NAME);
		ModificationWatchpointRequest modificationWatchpointRequest = erm
				.createModificationWatchpointRequest(field);
		modificationWatchpointRequest.setEnabled(true);
	}
	
	private static void handleModificationWatchPointEvent(VirtualMachine vm, ModificationWatchpointEvent event) {
		// a Test.foo has changed
		ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent)event;
		System.out.println("old=" + modEvent.valueCurrent());
		System.out.println("new=" + modEvent.valueToBe());
		System.out.println();
	}
	
	private static ThreadsMap threads = new ThreadsMap();
	
	private static void handleStepEvent(VirtualMachine vm, StepEvent event) {		
		Location location = event.location();
		
		try {
			System.out.println("Step: " + location.sourcePath() + ":" + location.lineNumber() + " (" 
				+ location.method() + ")");
		} catch (AbsentInformationException e) {
			System.out.println("No location information available for this step");
		}
			
		for (ThreadReference thread : vm.allThreads()) {	
			String threadName = thread.name();
			
			System.out.println("\tThread: " + threadName);
			
			if (threads.containsKey(threadName) == false) {
				threads.put(threadName, new VariablesStack());
			}
			
			int frameCount;
			
			try {
				frameCount = thread.frameCount();
			} catch (IncompatibleThreadStateException e) {
				System.out.println("\t\tNo stack variables available for this thread");
				
				continue;
			}
			
			VariablesStack variablesStack = threads.get(threadName);
			
			if (variablesStack.size() < frameCount) {
				// Push a new VariablesMap
				variablesStack.push(new VariablesMap());
			} else if (variablesStack.size() > frameCount) {
				// Pop off a VariablesMap
				variablesStack.pop();
			}
			
			if (frameCount > 0) {				
				VariablesMap variablesMap = variablesStack.peek();
				
				try {
					StackFrame frame = thread.frame(0);
					
					try {
						for (LocalVariable variable : frame.visibleVariables()) {
							if (variablesMap.containsKey(variable.name())) {
								// Variable already existed
								
								Value oldValue = variablesMap.get(variable.name());
								Value newValue = frame.getValue(variable);
								
								// TODO: I think that this equals needs to be refined
								if (oldValue.equals(newValue) == false) {
									variablesMap.put(variable.name(), newValue);
									
									System.out.println("\t\t" + variable.name() + " = " + newValue + " (updated from " + oldValue + ")");
								}
							} else {
								// Variable was just declared
								
								variablesMap.put(variable.name(), frame.getValue(variable));
								
								System.out.println("\t\t" + variable.name() + " = " + frame.getValue(variable) + " (declared)");
							}
						}
					} catch (AbsentInformationException e) {
						System.out.println("\t\tNo stack variables available for this thread");
					} 
				} catch (IncompatibleThreadStateException e) {
					System.out.println("\t\tNo stack variables available for this thread");
				}
			}
		}
	}
}
