package events;

import java.util.List;

import log.Logger;

import com.sun.jdi.Field;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

public class ClassPrepareEventHandler implements IEventHandler {
	
	private VirtualMachine vm;
	private Logger logger;
	
	public ClassPrepareEventHandler(VirtualMachine vm, Logger logger) {
		this.vm = vm;
		
		this.logger = logger;
		
		requestEvents();
	}

	private void requestEvents() {
		EventRequestManager erm = vm.eventRequestManager();
		
		ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
		classPrepareRequest.addClassFilter(MainEventHandler.CLASS_NAME);
		classPrepareRequest.enable();
	}

	@Override
	public int handle(Event event) {
		ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
		ReferenceType refType = classPrepEvent.referenceType();
		addFieldWatch(refType);
		
		return 0;
	}
	
	private void addFieldWatch(ReferenceType refType) {
		EventRequestManager erm = vm.eventRequestManager();
		List<Field> fields = refType.allFields();
		for(Field field : fields) {
			ModificationWatchpointRequest modificationWatchpointRequest = erm
					.createModificationWatchpointRequest(field);
			modificationWatchpointRequest.setEnabled(true);
		}
	}
}
