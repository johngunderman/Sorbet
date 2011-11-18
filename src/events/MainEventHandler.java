package events;

import log.Logger;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;

public class MainEventHandler implements IEventHandler {
	
	private VirtualMachine vm;
	
	private ClassPrepareEventHandler classPrepareEventHandler;
	private ModificationWatchpointEventHandler modificationWatchpointEventHandler;
	private StepEventHandler stepEventHandler;
	
	private Logger logger;
	
	public MainEventHandler(VirtualMachine vm, Logger logger) {
		this.vm = vm;
		
		classPrepareEventHandler = new ClassPrepareEventHandler(vm, logger);
		modificationWatchpointEventHandler = new ModificationWatchpointEventHandler(vm, logger);
		stepEventHandler = new StepEventHandler(vm, logger);
		
		this.logger = logger;
	}	

	public static final String CLASS_NAME = "client.Main";

	@Override
	public int handle(Event event) {		
		if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
			// exit
			return -1;
		} else if (event instanceof ClassPrepareEvent) {
			return classPrepareEventHandler.handle((ClassPrepareEvent)event);
		} else if (event instanceof ModificationWatchpointEvent) {
			return modificationWatchpointEventHandler.handle((ModificationWatchpointEvent)event);
		} else if (event instanceof StepEvent) {
			return stepEventHandler.handle((StepEvent)event);
		}
		
		return 0;
	}

}
