package log;

public abstract class Logger {

	protected int line = 0;
	
	void nextLine() {
		line++;
	}

	public abstract void logProgramStart(String programName, String args, String whitelist, String blacklist);

	public abstract void logVarCreated(String name, String type);

	public abstract void logVarChanged(String var, String value);

	public abstract void logVarDeath(String var);

	public abstract void logVarUsed(String var);

	public abstract void logProgramExit(int exitCode, String exception);

	public abstract void logLines(String filePath, int lineNum);
		
}
