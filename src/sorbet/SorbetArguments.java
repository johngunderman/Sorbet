package sorbet;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class SorbetArguments {
	@Parameter
	public List<String> parameters = Lists.newArrayList();

	@Parameter(names = "--source", description = "Path(s) to base directories containing source code to be profiled.  Paths should be separated by the default charecter for your OS", required = true)
	public List<String> source;
	
	@Parameter(names = "--main", description = "Fully qualified name of the class containing the main method for the client program", required = true)
	public String main;

	@Parameter(names = "--arguments", description = "Arguments to pass to the client program")
	public String arguments;
	
	@Parameter(names = "--classpath", description = "Classpath to use for the client program")
	public String classpath;
	
	@Parameter(names = "--blacklist", description = "Regular expressions for classes to black-list from profiling")
	public List<String> blacklist;
	
	@Parameter(names = "--whitelist", description = "Regular expressions for classes to white-list for profiling")
	public List<String> whitelist;
	
	@Parameter(names = "--logger", description = "Output logging to use.  Valid loggers are console and sqlite.", validateWith = ValidLogger.class)
	public String logger = "console";
	
	@Parameter(names = "--help", description = "Print this screen and exit")
	public boolean help;
}

