package log;

public class PrintLogger implements Logger {

	@Override
	public int log(String filename, int lineno, String varname, Object newvalue) {
		// TODO Auto-generated method stub
		
		System.out.println(filename + ":" + lineno + " " + varname + " = " + newvalue);
		return 0;
	}

}
