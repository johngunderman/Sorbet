package mockjdi;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.request.EventRequest;

public class MockClassPrepareEvent implements ClassPrepareEvent {
	
	private ReferenceType referenceType;
	
	public MockClassPrepareEvent(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	@Override
	public ReferenceType referenceType() {
		return referenceType;
	}

	@Override
	public ThreadReference thread() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventRequest request() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

}
