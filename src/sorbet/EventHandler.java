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

public class EventHandler {	
	public static final String CLASS_NAME = "client.Main";	
	
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
	public static int eventHandler(VirtualMachine vm, Event event) {
		// TODO Auto-generated method stub
		
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
		System.out.println("name: " + modEvent.field().name());
		System.out.println("old=" + modEvent.valueCurrent());
		System.out.println("new=" + modEvent.valueToBe());
		System.out.println();
	}
	
	private static void handleStepEvent(VirtualMachine vm, StepEvent event) {
		Location location = event.location();
		try {
			System.out.println("Step: " + location.sourcePath() + ":" + location.lineNumber() + " (" 
					+ location.method() + ")");
			
			for (ThreadReference thread : vm.allThreads()) {								
				System.out.println("\tThread: " + thread.name());
				
				try {
					if (thread.frameCount() > 0) {
						StackFrame frame = thread.frame(0);
						
						try {
							for (LocalVariable var : frame.visibleVariables()) {
								System.out.println("\t\t" + var.name() + " = " + frame.getValue(var));
							}
						} catch (AbsentInformationException e) {
							System.out.println("\t\tNo stack variables available for this thread");
						} 
					}
				} catch (IncompatibleThreadStateException e) {
					System.out.println("\t\tNo stack variables available for this thread");
				}
			}
		} catch (AbsentInformationException e) {
			System.out.println("No location information available for this step");
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}	
	}
}
