package mockjdi;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.ModificationWatchpointRequest;

public class MockModificationWatchpointRequest implements
		ModificationWatchpointRequest {

	@Override
	public void addClassExclusionFilter(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassFilter(ReferenceType arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassFilter(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addInstanceFilter(ObjectReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addThreadFilter(ThreadReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Field field() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCountFilter(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getProperty(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putProperty(Object arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSuspendPolicy(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int suspendPolicy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

}
