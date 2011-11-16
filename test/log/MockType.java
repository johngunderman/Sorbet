package log;

import com.sun.jdi.Type;
import com.sun.jdi.VirtualMachine;

public class MockType implements Type {

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "int";
	}

	@Override
	public String signature() {
		// TODO Auto-generated method stub
		return null;
	}

}
