package log;

public interface Logger {

	public int log(String filename, int lineno, String varname, Object newvalue);
	
}
