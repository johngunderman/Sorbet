package events;

import java.util.LinkedList;
import java.util.Queue;

import com.sun.jdi.Value;

import log.Logger;

public class MockLogger implements Logger {
	
	public class Entry {
		
		public String filename;
		
		public int lineno;
		
		public String methodname;
		
		public String varname;
		
		public Value newvalue;
		
		public Entry(String filename, int lineno, String methodname,
				String varname, Value newvalue) {
			this.filename = filename;
			this.lineno = lineno;
			this.methodname = methodname;
			this.varname = varname;
			this.newvalue = newvalue;
		}
		
	}
	
	private Queue<Entry> logEntries;
	
	public MockLogger() {
		this.logEntries = new LinkedList<Entry>();
	}

	@Override
	public int log(String filename, int lineno, String methodname,
			String varname, Value newvalue) {
		logEntries.offer(new Entry(filename, lineno, methodname, varname, newvalue));
		
		return 0;
	}
	
	public Entry getEntry() {
		return logEntries.poll();
	}

}
