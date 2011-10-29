package sorbet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

public class Main {	
	public static void main(String args[]) {		
		VirtualMachine vm = launchVirtualMachine("client.Main");
		
		vm.resume();
			
		InputStream errorStream = vm.process().getErrorStream();
		InputStream outputStream = vm.process().getInputStream();
		
		while (true) {
			int error = errorStream.read();
			if (error != -1) {
				System.out.print((char)error);
			}
			
			int output = outputStream.read();
			if (output != -1) {
				System.out.print((char)output);
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
}
