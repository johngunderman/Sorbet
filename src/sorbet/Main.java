package sorbet;

import java.io.IOException;
import java.util.Map;

import log.PrintLogger;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;

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
			new StreamTunnel(vm.process().getInputStream(), System.out);
			new StreamTunnel(vm.process().getErrorStream(), System.err);
			
			return vm;
		} catch (IOException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        } catch (IllegalConnectorArgumentsException e) {
			StringBuffer args = new StringBuffer();
			
			for (String arg : e.argumentNames()) {
				args.append(arg);
			}
			
            throw new Error("Error: Could not launch target VM because of illegal arguments: " + args.toString());
        } catch (VMStartException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        }
	}
	
	private static void debugLoop(VirtualMachine vm) {
		vm.resume();

		while (true) {
			try {
				EventSet eventSet = vm.eventQueue().remove();
				for (Event event : eventSet) {
					if (EventHandler.handle(vm,event) == -1) {
						return;
					}
					
				}
					
				eventSet.resume();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
