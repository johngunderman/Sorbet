package main;

import java.io.IOException;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.connect.Connector.Argument;

public class Sorbet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
		LaunchingConnector lc = vmManager.defaultConnector();
		
		try {
			Map<String, Argument> da = lc.defaultArguments();
			da.get("main").setValue("");
			VirtualMachine vm = lc.launch(da);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalConnectorArgumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VMStartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}