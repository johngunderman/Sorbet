package sorbet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import sourceparser.SourceParser;

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
	
	SourceParser sourceParser;	
	Logger logger;
	VirtualMachine vm;
	
	public Sorbet() {
	
	}
	
	public void setSourceParser(SourceParser sourceParser) {
		this.sourceParser = sourceParser;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}
	
	public void run() {

		MainEventHandler mainEventHandler = new MainEventHandler(sourceParser, logger, vm);
		
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
