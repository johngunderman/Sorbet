package sorbet;

import java.util.LinkedList;
import java.util.List;

import sourceparser.SourceParser;
import events.MainEventHandler;

public class Main {	
	public static void main(String args[]) {
		List<String> sourcePaths = new LinkedList<String>();
		sourcePaths.add("../src/client");
		
		Sorbet sorbet = new Sorbet(sourcePaths, "client.Main");
		
		sorbet.runClient();
	}
}
