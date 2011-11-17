package sorbet;

import java.io.IOException;
import java.util.Map;

import log.Logger;
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

import events.MainEventHandler;

public class Sorbet {
	
	VirtualMachine vm;
	Logger logger;
	MainEventHandler mainEventHandler;
	
	public Sorbet() {
		launchVirtualMachine(MainEventHandler.CLASS_NAME);	
		
		logger = new PrintLogger();
		
		mainEventHandler = new MainEventHandler(vm, logger);
	}


	private void launchVirtualMachine(String mainArg) {
		
		VirtualMachineManager manager = Bootstrap.virtualMachineManager();
		
		LaunchingConnector connector = manager.defaultConnector();
		
		Map<String, Connector.Argument> arguments = connector.defaultArguments();
		
		arguments.get("main").setValue(mainArg);
		
		try {
			vm = connector.launch(arguments);
			
			// Forward standard out and standard error
			new StreamTunnel(vm.process().getInputStream(), System.out);
			new StreamTunnel(vm.process().getErrorStream(), System.err);
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
	
	public void runClient() {
		vm.resume();

		while (true) {
			try {
				EventSet eventSet = vm.eventQueue().remove();
				for (Event event : eventSet) {
					if (mainEventHandler.handle(event) == -1) {
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
