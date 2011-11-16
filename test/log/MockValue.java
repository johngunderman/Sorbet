package log;

import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;

public class MockValue implements Value {

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type type() {
		// TODO Auto-generated method stub
		return new MockType();
	}
	
	@Override
	public String toString() {
		return "32";
		
	}

}
