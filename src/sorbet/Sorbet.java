package sorbet;

import sourceparser.SourceParser;

import log.Logger;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;

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

		StepEventHandler stepEventHandler = new StepEventHandler(sourceParser, logger, vm);
		
		vm.resume();

		while (true) {
			try {
				EventSet eventSet = vm.eventQueue().remove();
				
				for (Event event : eventSet) {
					if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
						// exit
						return;
					} 
					
					stepEventHandler.handle(event);					
				}
					
				eventSet.resume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
