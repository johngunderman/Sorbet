package log;

import com.sun.jdi.Value;

public abstract class Logger {

	private int line = 0;
	
	void nextLine() {
		line++;
	}

	public abstract void logProgramStart(String programName, String userName, String args);

	public abstract void logVarCreated(String value);

	public abstract void logVarChanged(String var, String value);

	public abstract void logVarDeath(String var);

	public abstract void logVarUsed(String var);

	public abstract void logProgramExit(int runId, int exitCode, String exception);

	public abstract void logLines(int runId, String filePath, int lineNum);
		
}
