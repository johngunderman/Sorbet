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
import com.sun.jdi.event.EventQueue;
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


	public static void main(String args[]) {		
		VirtualMachine vm = launchVirtualMachine(EventHandler.CLASS_NAME);		
		
		EventHandler.requestEvents(vm);
		
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
	
	private static void debugLoop(VirtualMachine vm) {
		vm.resume();

		while (true) {
			try {
				EventSet eventSet = vm.eventQueue().remove();
				for (Event event : eventSet) {
					if (EventHandler.eventHandler(vm,event) == -1) {
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
	

}
