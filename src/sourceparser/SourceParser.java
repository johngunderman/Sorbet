package sourceparser;

import java.io.File;
import java.io.FileFilter;
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
	
	public int addRootPaths(String rootPaths) {
		int failed = 0;
		
		for (String path : rootPaths.split(File.pathSeparator)) {
			failed += addPath(path, "");
		}
		
		return failed;
	}
	
	private int addPath(String rootPath, String subPath) {
		int failed = 0;
		
		File directory = new File(rootPath, subPath);
		
		if (directory.exists() == false) {
			return 1;
		}
		
		File[] files = directory.listFiles();
		
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".java")) {
					String relativeFilePath = subPath + File.separator + file.getName();					
					while (relativeFilePath.startsWith("/")) {
						relativeFilePath = relativeFilePath.substring(1);
					}
					
					failed += addFile(rootPath, relativeFilePath);
				} else if (file.isDirectory()) {
					failed += addPath(rootPath, subPath + File.separator + file.getName());
				}
			}
		}
		
		return failed;
	}
	
	private int addFile(String rootPath, String relativeFilePath) {		
		try {
			FileInputStream file = new FileInputStream(rootPath + File.separator + relativeFilePath);
			
			CompilationUnit cu = JavaParser.parse(file);
			
			VariableVisitor<Object> variableVisitor = new VariableVisitor<Object>();
			variableVisitor.visit(cu, null);
			
			Lines lines = variableVisitor.getLines();
			
			if (files.containsKey(relativeFilePath)) {
				return 1;
			} else {
				files.put(relativeFilePath, lines);
			}			
			
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
