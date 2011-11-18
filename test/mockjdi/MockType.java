package mockjdi;

import com.sun.jdi.Type;
import com.sun.jdi.VirtualMachine;

public class MockType implements Type {
	
	String type;
	
	public MockType(String type) {
		this.type = type;
	}

	@Override
	public VirtualMachine virtualMachine() {
		return null;
	}

	@Override
	public String name() {
		return type;
	}

	@Override
	public String signature() {
		return null;
	}

}
