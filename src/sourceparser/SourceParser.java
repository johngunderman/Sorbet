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
	
	public int addPaths(String paths) {
		return addPaths(paths, true);
	}
	
	public int addPaths(String paths, boolean recursive) {
		List<String> pathList = new LinkedList<String>();
		
		for (String path : paths.split(File.pathSeparator)) {
			pathList.add(path);
		}
		
		return addPaths(pathList, recursive);
	}
	
	public int addPaths(List<String> paths) {
		return addPaths(paths, true);
	}
	
	public int addPaths(List<String> paths, boolean recursive) {
		int failed = 0;
		
		for (String path : paths) {
			failed += addPath(path, recursive);
		}
		return failed;
	}
	
	public int addPath(String path) {
		return addPath(path, true);
	}
	
	public int addPath(String path, boolean recursive) {
		
		int failed = 0;
		
		File directory = new File(path);
		File[] files = directory.listFiles();
		
		if (directory != null && files != null) {
			for (File file : files) {
				if (file.isFile()) {
					failed += addFile(file.getPath());
				}
				else if (recursive) {
					failed += addPath(path + File.separator + file.getName(), recursive);
				}
			}
		}
		
		return failed;
	}
	
	public int addFile(String filePath) {		
		try {
			FileInputStream file = new FileInputStream(filePath);
			
			CompilationUnit cu = JavaParser.parse(file);
			
			VariableVisitor<Object> variableVisitor = new VariableVisitor<Object>();
			variableVisitor.visit(cu, null);
			
			Lines lines = variableVisitor.getLines();			
			
			files.put(filePath, lines);
			
		} catch (FileNotFoundException e) {
			return 1;
		} catch (ParseException e) {
			return 1;
		}
		
		return 0;		
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
