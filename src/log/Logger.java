package log;

import com.sun.jdi.Value;

public interface Logger {

	public int log(String filename, int lineno, String methodname, String varname, Value newvalue);
	
}
