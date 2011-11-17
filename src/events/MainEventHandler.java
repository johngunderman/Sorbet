package events;

import log.Logger;
import log.PrintLogger;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

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
	
	public void requestEvents() {
		EventRequestManager erm = vm.eventRequestManager();
		ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
		classPrepareRequest.addClassFilter(CLASS_NAME);
		classPrepareRequest.enable();		
	}	

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
