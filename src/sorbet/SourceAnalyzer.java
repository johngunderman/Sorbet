package sorbet;

import java.io.FileInputStream;
import java.io.IOException;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;


public class SourceAnalyzer {

	static String FILE = "src/client/Main.java" ;
	
	public static void main(String [] argv) throws ParseException, IOException {
		FileInputStream in = new FileInputStream(FILE);
		
		CompilationUnit cu = JavaParser.parse(in);
		
		in.close();
		
		new VariableVisitor().visit(cu, null);
		
	}
}
