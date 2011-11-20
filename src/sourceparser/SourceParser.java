package sourceparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;


import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;


public class SourceParser {
	
	private Files files = new Files();
	
	public SourceParser() {
		
	}
	
	public boolean addPaths(List<String> paths) {
		return addPaths(paths, true);
	}
	
	public boolean addPaths(List<String> paths, boolean recursive) {
		boolean success = true;
		
		for (String path : paths) {
			if (addPath(path, recursive) == false) {
				success = false;
			}
		}
		return success;
	}
	
	public boolean addPath(String path) {
		return addPath(path, true);
	}
	
	public boolean addPath(String path, boolean recursive) {

		boolean success = true;
		
		File directory = new File(path);
		
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				if (addFile(file.getPath()) == false) {
					success = false;
				}
			}
			else if (recursive) {
				if (addPath(path, recursive) == false) {
					success = false;
				}
			}
		}
		
		return success;
	}
	
	public boolean addFile(String filePath) {		
		try {
			FileInputStream file = new FileInputStream(filePath);
			
			CompilationUnit cu = JavaParser.parse(file);
			
			VariableVisitor<Object> variableVisitor = new VariableVisitor<Object>();
			variableVisitor.visit(cu, null);
			
			Lines lines = variableVisitor.getLines();			
			
			files.put(filePath, lines);
			
		} catch (FileNotFoundException e) {
			return false;
		} catch (ParseException e) {
			return false;
		}
		
		return true;		
	}
	
	public List<String> getVariables(String filePath, int lineNumber) {
		Lines lines = files.get(filePath);		
		if (lines == null) {
			return null;
		}
		
		Variables variables = lines.get(lineNumber);		
		return variables;
	}
}
