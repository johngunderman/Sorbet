package events;

import log.Logger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ModificationWatchpointEvent;

public class ModificationWatchpointEventHandler implements IEventHandler {
	
	private VirtualMachine vm;
	
	private Logger logger;
	
	public ModificationWatchpointEventHandler(VirtualMachine vm, Logger logger) {
		this.vm = vm;
		
		this.logger = logger;
		
		// TODO: The events for this are requested in ClassPrepareEventHandler.  Merge these classes?
	}

	@Override
	public int handle(Event event) {
		ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent)event;
		
		try {
			logger.log(modEvent.location().sourcePath(), modEvent.location().lineNumber(), modEvent.location().method().name(), 
						modEvent.field().name(), modEvent.valueToBe());
		} catch (AbsentInformationException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot access current filename!");
			e.printStackTrace();
		}
		
		return 0;
	}
}
