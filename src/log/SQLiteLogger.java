package log;

import com.sun.jdi.Value;
import java.sql.*;

public class SQLiteLogger extends Logger {


	private static String createRuns = "CREATE TABLE IF NOT EXISTS RUNS ( " +
			"runsid INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"programname TEXT, " +
			"args TEXT, " +
			"whitelist TEXT, " +
			"blacklist TEXT, " +
			"username TEXT, " +
			"timestamp TEXT)";

	private static String createLines = "CREATE TABLE IF NOT EXISTS LINES ( " +
			"runsid INTEGER REFERENCES RUNS (runsid), " +
			"linesid INTEGER KEY," +
			"filepath TEXT," +
			"linenum INTEGER, " +
			"timestamp TEXT, " +
			"PRIMARY KEY (runsid, linesid))";

	private static String createVars = "CREATE TABLE IF NOT EXISTS VARS ( " +
			"runsid INTEGER REFERENCES RUNS (runsid), " +
			"varsid INTEGER, " +
			"linesid INTEGER REFERENCES LINES (linesid), " +
			"name TEXT," +
			"type TEXT, " +
			"PRIMARY KEY (runsid, varsid))";

	private static String createVarValues = "CREATE TABLE IF NOT EXISTS VARVALUES ( " +
			"runsid INTEGER REFERENCES RUNS (runsid)," +
			"varvaluesid INTEGER," +
			"varsid INTEGER REFERENCES VARS (varsid), " +
			"linesid INTEGER REFERENCES LINES (linesid)," +
			"value TEXT, " +
			"type TEXT," +
			"PRIMARY KEY (runsid, varvaluesid))";

	private static String createVarDeath = "CREATE TABLE IF NOT EXISTS VARDEATH (" +
			"runsid INTEGER REFERENCES RUNS (runsid)," +
			"vardeathid INTEGER," +
			"varsid INTEGER REFERENCES VARS (varsid)," +
			"linesid INTEGER REFERENCES LINES (linesid)," +
			"PRIMARY KEY (runsid, vardeathid))";

	private static String createVarUsed = "CREATE TABLE IF NOT EXISTS VARUSED (" +
			"runsid INTEGER REFERENCES RUNS (runsid)," +
			"linesid INTEGER," +
			"varsid INTEGER," +
			"PRIMARY KEY (runsid, linesid, varsid))" +
			"CREATE TABLE IF NOT EXISTS PROGRAMEXIT (" +
			"runsid INTEGER PRIMARY KEY REFERENCES RUNS (runsid)," +
			"exitcode INTEGER," +
			"exception TEXT)";
	

	private static String runsInsert = "INSERT INTO RUNS " +
			"(runsid, programname, args, whitelist, blacklist, username, timestamp) " +
			"VALUES (NULL, ?, ?, ?, ?, ?, ?)";

	private static String linesInsert = "INSERT INTO LINES " +
			"(runsid, linesid, filepath, linenum, timestamp) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static String varInsert = "INSERT INTO VARS " +
			"(runsid, varsid, linesid, name, type) " +
			"VALUES (?, ?, ?, ?, ?)";

	private static String  varValuesInsert = "INSERT INTO VARVALUES " +
			"(runsid, varvaluesid, varsid, linesid, value, type) " +
			"VALUES (?, ?, ?, ?, ?, ?)";

	private static String varDeathInsert = "INSERT INTO VARDEATH " +
			"(runsid, vardeathid, varsid, linesid)" +
			"VALUES (?, ?, ?, ?)";

	private static String varUsedInsert = "INSERT INTO VARUSED " +
			"(runsid, linesid, varsid) " +
			"VALUES (?, ?, ?)";

	private static String programExitInsert = "INSERT INTO PROGRAMEXIT " +
			"(runsid, exitcode, exception) " +
			"VALUES (?, ?, ?)";


	public SQLiteLogger() {
		// initialize db, make sure we exist
		Connection conn;

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:sorbet_out.db");
			Statement stmt = conn.createStatement();
			stmt.addBatch(createLines);
			stmt.addBatch(createRuns);
			stmt.addBatch(createVarDeath);
			stmt.addBatch(createVarUsed);
			stmt.addBatch(createVarValues);
			stmt.addBatch(createVars);
			stmt.executeBatch();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@Override 
	public void logProgramStart(String programName, String args, String whitelist, String blacklist) {
		
	}

	@Override
	public void logVarCreated(String value) {

	}

	@Override
	public void logVarChanged(String var, String value) {

	}

	@Override
	public void logVarDeath(String var) {

	}

	@Override
	public void logVarUsed(String var) {

	}

	@Override
	public void logProgramExit(int runId, int exitCode, String exception) {

	}

	@Override
	public void logLines(int runId, String filePath, int lineNum) {

	}

}
