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
import com.sun.xml.internal.bind.v2.util.EditDistance;

public class Main {	
	public static void main(String args[]) {		
		VirtualMachine vm = launchVirtualMachine("client.Main");
		
		vm.resume();
			
		//InputStream errorStream = vm.process().getErrorStream();
		//InputStream outputStream = vm.process().getInputStream();
		
		StreamTunnel errorStreamTunnel = new StreamTunnel(vm.process().getErrorStream(), System.err);
		StreamTunnel outputStreamTunnel = new StreamTunnel(vm.process().getInputStream(), System.out);
		
		Thread errorStreamThread = new Thread(errorStreamTunnel);
		Thread outputStreamThread = new Thread(outputStreamTunnel);
		
		errorStreamThread.start();
		outputStreamThread.start();
		
		while (true) {
			
		}	
		
		//vm.exit(0);
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
