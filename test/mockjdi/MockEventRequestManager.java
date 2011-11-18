package mockjdi;

import java.util.List;

import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.ClassUnloadRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.MonitorContendedEnterRequest;
import com.sun.jdi.request.MonitorContendedEnteredRequest;
import com.sun.jdi.request.MonitorWaitRequest;
import com.sun.jdi.request.MonitorWaitedRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;
import com.sun.jdi.request.VMDeathRequest;

public class MockEventRequestManager implements EventRequestManager {

	@Override
	public List<AccessWatchpointRequest> accessWatchpointRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BreakpointRequest> breakpointRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClassPrepareRequest> classPrepareRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClassUnloadRequest> classUnloadRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessWatchpointRequest createAccessWatchpointRequest(Field arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BreakpointRequest createBreakpointRequest(Location arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassPrepareRequest createClassPrepareRequest() {
		return new MockClassPrepareRequest();
	}

	@Override
	public ClassUnloadRequest createClassUnloadRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExceptionRequest createExceptionRequest(ReferenceType arg0,
			boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEntryRequest createMethodEntryRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodExitRequest createMethodExitRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModificationWatchpointRequest createModificationWatchpointRequest(
			Field arg0) {
		return new MockModificationWatchpointRequest();
	}

	@Override
	public MonitorContendedEnterRequest createMonitorContendedEnterRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitorContendedEnteredRequest createMonitorContendedEnteredRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitorWaitRequest createMonitorWaitRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitorWaitedRequest createMonitorWaitedRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StepRequest createStepRequest(ThreadReference arg0, int arg1,
			int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThreadDeathRequest createThreadDeathRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThreadStartRequest createThreadStartRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VMDeathRequest createVMDeathRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllBreakpoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEventRequest(EventRequest arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEventRequests(List<? extends EventRequest> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ExceptionRequest> exceptionRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MethodEntryRequest> methodEntryRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MethodExitRequest> methodExitRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ModificationWatchpointRequest> modificationWatchpointRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitorContendedEnterRequest> monitorContendedEnterRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitorWaitRequest> monitorWaitRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MonitorWaitedRequest> monitorWaitedRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StepRequest> stepRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThreadDeathRequest> threadDeathRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThreadStartRequest> threadStartRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VMDeathRequest> vmDeathRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return null;
	}

}
