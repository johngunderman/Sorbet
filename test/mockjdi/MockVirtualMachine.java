package mockjdi;

import java.util.List;
import java.util.Map;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VoidValue;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.EventRequestManager;

public class MockVirtualMachine implements VirtualMachine {

	@Override
	public List<ReferenceType> allClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThreadReference> allThreads() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAddMethod() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeModified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canForceEarlyReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetBytecodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetClassFileVersion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetConstantPool() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetCurrentContendedMonitor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetInstanceInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetMethodReturnValues() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetMonitorFrameInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetMonitorInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetOwnedMonitorInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetSourceDebugExtension() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGetSyntheticAttribute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPopFrames() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRedefineClasses() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRequestMonitorEvents() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRequestVMDeathEvent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUnrestrictedlyRedefineClasses() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseInstanceFilters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseSourceNameFilters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWatchFieldAccess() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWatchFieldModification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReferenceType> classesByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public EventQueue eventQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	private EventRequestManager mockEventRequestManager = new MockEventRequestManager();	
	@Override
	public EventRequestManager eventRequestManager() {
		// TODO Auto-generated method stub
		return mockEventRequestManager;
	}

	@Override
	public void exit(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDefaultStratum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long[] instanceCounts(List<? extends ReferenceType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanValue mirrorOf(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteValue mirrorOf(byte arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharValue mirrorOf(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShortValue mirrorOf(short arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerValue mirrorOf(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongValue mirrorOf(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FloatValue mirrorOf(float arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleValue mirrorOf(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringReference mirrorOf(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoidValue mirrorOfVoid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Process process() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redefineClasses(Map<? extends ReferenceType, byte[]> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDebugTraceMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultStratum(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ThreadGroupReference> topLevelThreadGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

}
