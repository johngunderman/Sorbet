package sourceparser;

public class Main {
	
	public static void main(String [] argv) {
		
		SourceParser sourceParser = new SourceParser();
		
		sourceParser.addRootPaths("src/client/");
		
		for (String variable : sourceParser.getVariables("Main.java", 50)) {
			System.out.print(variable + ", ");
		}
	}
}
