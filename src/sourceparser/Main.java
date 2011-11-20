package sourceparser;

public class Main {
	
	public static void main(String [] argv) {
		
		SourceParser sourceParser = new SourceParser();
		
		sourceParser.addPath("src/client/");
		
		for (String variable : sourceParser.getVariables("src/client/Main.java", 50)) {
			System.out.print(variable + ", ");
		}
	}
}
