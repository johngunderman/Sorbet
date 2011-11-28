package log;

public class PrintLogger extends Logger {

	@Override
	public void logProgramStart(String programName, String args, String whitelist, String blacklist) {
		// TODO Auto-generated method stub
		System.out.println("Program started with name [" + programName + "] and args [" + args + "]");
		System.out.println("Whitelist: " + whitelist);
		System.out.println("Blacklist: " + blacklist);
		System.out.println("=============================");
	}

	@Override
	public void logVarCreated(String name, String type) {
		// TODO Auto-generated method stub
		System.out.println("Variable created: [" + type + "] " + name );
	}

	@Override
	public void logVarChanged(String var, String value) {
		// TODO Auto-generated method stub
		System.out.println(">>> " + var + " = " + value);
	}

	@Override
	public void logVarDeath(String var) {
		// TODO Auto-generated method stub
		System.out.println("Var Death: " + var);
	}

	@Override
	public void logVarUsed(String var) {
		// TODO Auto-generated method stub
		System.out.println("Var Used: " + var);
	}

	@Override
	public void logProgramExit(int exitCode, String exception) {
		// TODO Auto-generated method stub
		System.out.println("Program Exited with code " + exitCode + " [" + exception + "]");
	}

	@Override
	public void logLines(String filePath, int lineNum) {
		// TODO Auto-generated method stub
		System.out.println("Executing: " + filePath + ":" + lineNum);
	}


}
