package log;

import com.sun.jdi.Value;

public class PrintLogger implements Logger {

	@Override
	public int log(String filename, int lineno, String methodname, String varname, Value newvalue) {
		// TODO Auto-generated method stub
		
		System.out.println(filename + ":" + lineno + " in " + methodname + "(), [" + newvalue.type().name() + "] " + varname + " = " + newvalue);
		return 0;
	}

}
