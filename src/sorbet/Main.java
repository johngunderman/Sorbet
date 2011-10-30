package sorbet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.xml.internal.bind.v2.util.EditDistance;


public class Main {	

	public static final String FIELD_NAME = "foo";
	public static final String CLASS_NAME = "client.Main";
	
	public static void main(String args[]) {		
		VirtualMachine vm = launchVirtualMachine(CLASS_NAME);
	
		List<ReferenceType> referenceTypes = vm.classesByName(CLASS_NAME);
		for (ReferenceType refType : referenceTypes) {
			System.out.println(refType);
			//addFieldWatch(vm, refType);
		}
		
		
		EventRequestManager erm = vm.eventRequestManager();
		ClassPrepareRequest classPrepareRequest = erm
				.createClassPrepareRequest();
		classPrepareRequest.addClassFilter(CLASS_NAME);
		classPrepareRequest.setEnabled(true);
					
		//InputStream errorStream = vm.process().getErrorStream();
		//InputStream outputStream = vm.process().getInputStream();
		
		StreamTunnel errorStreamTunnel = new StreamTunnel(vm.process().getErrorStream(), System.err);
		StreamTunnel outputStreamTunnel = new StreamTunnel(vm.process().getInputStream(), System.out);
		
		Thread errorStreamThread = new Thread(errorStreamTunnel);
		Thread outputStreamThread = new Thread(outputStreamTunnel);
		
		errorStreamThread.start();
		outputStreamThread.start();
		
		for (ThreadReference ref : vm.allThreads()) {
			if (ref.name().equals("main")) {
				StepRequest request = erm.createStepRequest(ref, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
				request.addClassFilter("client.Main");
				request.enable();
			}
		}
		
		vm.resume();
		
	    EventQueue eventQueue = vm.eventQueue();

		while (true) {
			EventSet eventSet;
			try {
				eventSet = eventQueue.remove();
				for (Event event : eventSet) {
					if (event instanceof VMDeathEvent
							|| event instanceof VMDisconnectEvent) {
						// exit
						return;
					} else if (event instanceof ClassPrepareEvent) {
						// watch field on loaded class
						ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
						ReferenceType refType = classPrepEvent.referenceType();
						addFieldWatch(vm, refType);
					} else if (event instanceof ModificationWatchpointEvent) {
						// a Test.foo has changed
						ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent) event;
						System.out.println("old=" + modEvent.valueCurrent());
						System.out.println("new=" + modEvent.valueToBe());
						System.out.println();
					}
					else if (event instanceof StepEvent) {
						Location loc = ((StepEvent)event).location();
						try {
							System.out.println("step: " + loc.sourceName() + "." + loc.lineNumber());
						} catch (AbsentInformationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						for (ThreadReference ref : vm.allThreads()) {
							if (ref.name().equals("main")) {
								try {
									StackFrame frame = ref.frame(0);
									
									try {
										for (LocalVariable var : frame.visibleVariables()) {
											System.out.println(var.name() + " : " + frame.getValue(var).type());
										}
									} catch (AbsentInformationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} catch (IncompatibleThreadStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				eventSet.resume();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	
	private static VirtualMachine launchVirtualMachine(String mainArg) {
		VirtualMachineManager manager = Bootstrap.virtualMachineManager();
		
		LaunchingConnector connector = manager.defaultConnector();
		
		Map<String, Connector.Argument> arguments = connector.defaultArguments();
		
		arguments.get("main").setValue(mainArg);
		
		try {
			VirtualMachine vm = connector.launch(arguments);
			
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
	
	private static void addFieldWatch(VirtualMachine vm, ReferenceType refType) {
		EventRequestManager erm = vm.eventRequestManager();
		Field field = refType.fieldByName(FIELD_NAME);
		ModificationWatchpointRequest modificationWatchpointRequest = erm
				.createModificationWatchpointRequest(field);
		modificationWatchpointRequest.setEnabled(true);
	}
}
