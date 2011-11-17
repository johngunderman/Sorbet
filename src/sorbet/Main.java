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

public class Main {	
	public static void main(String args[]) {	
		Sorbet sorbet = new Sorbet();
		
		sorbet.runClient();
	}		
}
