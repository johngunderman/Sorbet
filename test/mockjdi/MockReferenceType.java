package mockjdi;

import java.util.List;
import java.util.Map;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.ClassObjectReference;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;

public class MockReferenceType implements ReferenceType {
	
	private String referenceType;
	
	private List<Field> allFields;
	
	public MockReferenceType(String referenceType, List<Field> allFields) {
		this.referenceType = referenceType;
		
		this.allFields = allFields;
	}

	@Override
	public List<Field> allFields() {
		return allFields;
	}

	@Override
	public List<Location> allLineLocations() throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Location> allLineLocations(String arg0, String arg1)
			throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Method> allMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> availableStrata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoaderReference classLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassObjectReference classObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] constantPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int constantPoolCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String defaultStratum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean failedToInitialize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Field fieldByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Field> fields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genericSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value getValue(Field arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Field, Value> getValues(List<? extends Field> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectReference> instances(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFinal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrepared() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVerified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Location> locationsOfLine(int arg0)
			throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Location> locationsOfLine(String arg0, String arg1, int arg2)
			throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int majorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Method> methods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Method> methodsByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Method> methodsByName(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int minorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReferenceType> nestedTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sourceDebugExtension() throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sourceName() throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> sourceNames(String arg0)
			throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> sourcePaths(String arg0)
			throws AbsentInformationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Field> visibleFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Method> visibleMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String signature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(ReferenceType arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPackagePrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isProtected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPublic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int modifiers() {
		// TODO Auto-generated method stub
		return 0;
	}

}
