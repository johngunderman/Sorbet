package sorbet;

import java.io.IOException;
import java.util.Map;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;

public class Main {	

	public static final String FIELD_NAME = "foo";
	public static final String CLASS_NAME = "client.Main";
	
	public static void main(String args[]) {		
		VirtualMachine vm = launchVirtualMachine(CLASS_NAME);		
		
		requestEvents(vm);
		
		debugLoop(vm);
	}
		
	
	private static VirtualMachine launchVirtualMachine(String mainArg) {
		VirtualMachineManager manager = Bootstrap.virtualMachineManager();
		
		LaunchingConnector connector = manager.defaultConnector();
		
		Map<String, Connector.Argument> arguments = connector.defaultArguments();
		
		arguments.get("main").setValue(mainArg);
		
		try {
			VirtualMachine vm = connector.launch(arguments);
			
			// Forward standard out and standard error
			StreamTunnel outputStreamTunnel = new StreamTunnel(vm.process().getInputStream(), System.out);
			StreamTunnel errorStreamTunnel = new StreamTunnel(vm.process().getErrorStream(), System.err);
			
			return vm;
		}
		catch (IOException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        } 
		catch (IllegalConnectorArgumentsException e) {
			StringBuffer args = new StringBuffer();
			
			for (String arg : e.argumentNames()) {
				args.append(arg);
			}
			
            throw new Error("Error: Could not launch target VM because of illegal arguments: " + args.toString());
        } 
		catch (VMStartException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        }
	}
	
	private static void requestEvents(VirtualMachine vm) {
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
	
	private static void debugLoop(VirtualMachine vm) {
		vm.resume();

		while (true) {
			try {
				EventSet eventSet = vm.eventQueue().remove();
				for (Event event : eventSet) {
					if (eventHandler(vm,event) == -1) {
						return;
					}
					
				}
					
				eventSet.resume();				
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
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
	private static int eventHandler(VirtualMachine vm, Event event) {
		// TODO Auto-generated method stub
		
		if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
			// exit
			return -1;
		} else if (event instanceof ClassPrepareEvent) {
			handleClassPrepareEvent(vm, (ClassPrepareEvent)event);
		} else if (event instanceof ModificationWatchpointEvent) {
			handleModificationWatchPointEvent(vm, (ModificationWatchpointEvent)event);
		}
		else if (event instanceof StepEvent) {
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
						} 
						catch (AbsentInformationException e) {
							System.out.println("\t\tNo stack variables available for this thread");
						} 
					}
				} catch (IncompatibleThreadStateException e) {
					System.out.println("\t\tNo stack variables available for this thread");
				}
			}
		} 
		catch (AbsentInformationException e) {
			System.out.println("No location information available for this step");
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}	
	}
}
