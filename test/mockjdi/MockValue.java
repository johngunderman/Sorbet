package mockjdi;

import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;

public class MockValue implements Value {
	
	private Type type;
	
	private String value;
	
	public MockValue(Type mockType, String value) {
		this.type = mockType;
		
		this.value = value;
	}

	@Override
	public VirtualMachine virtualMachine() {
		return null;
	}

	@Override
	public Type type() {
		return type;
	}
	
	@Override
	public String toString() {
		return value;		
	}

}
