package sorbet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import log.PrintLogger;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

import sourceparser.SourceParser;
import events.MainEventHandler;

public class Main {	
	public static void main(String args[]) {
		
		Sorbet sorbet = new Sorbet();

		sorbet.setSourceParser(createSourceParser());
		
		sorbet.setLogger(new PrintLogger());
		
		sorbet.setVirtualMachine(createVirtualMachine("client.Main", ""));
		
		sorbet.run();
	}
	
	public static SourceParser createSourceParser() {		
		SourceParser sourceParser = new SourceParser();
		sourceParser.addRootPaths("../src");
		
		return sourceParser;
	}
	
	public static VirtualMachine createVirtualMachine(String main, String args) {
		
		VirtualMachineManager manager = Bootstrap.virtualMachineManager();
		
		LaunchingConnector connector = manager.defaultConnector();
		
		Map<String, Connector.Argument> arguments = connector.defaultArguments();
		 	
		arguments.get("main").setValue(main);
		arguments.get("options").setValue(args);
		
		try {
			VirtualMachine vm = connector.launch(arguments);
			
			// Forward standard out and standard error
			new StreamTunnel(vm.process().getInputStream(), System.out);
			new StreamTunnel(vm.process().getErrorStream(), System.err);
			
			return vm;
		} catch (IOException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        } catch (IllegalConnectorArgumentsException e) {
			StringBuffer illegalArguments = new StringBuffer();
			
			for (String arg : e.argumentNames()) {
				illegalArguments.append(arg);
			}
			
            throw new Error("Error: Could not launch target VM because of illegal arguments: " + illegalArguments.toString());
        } catch (VMStartException e) {
            throw new Error("Error: Could not launch target VM: " + e.getMessage());
        }
	}
}
