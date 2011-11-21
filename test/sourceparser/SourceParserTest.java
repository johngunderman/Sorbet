package sourceparser;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SourceParserTest {
	
	private static final String basePath = "test/sourceparser/testfiles/";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddPathsString() {
		SourceParser sourceParser = new SourceParser();	
		
		// Test adding a single path that doesn't exist
		String paths = basePath + "thisDirectoryDoesntExist";
		assertEquals(0, sourceParser.addPaths(paths));		
		
		// Test adding two paths that don't exist
		paths += ":" + basePath + "thisDirectoryAlsoDoesntExist";
		assertEquals(0, sourceParser.addPaths(paths));	
		
		// Test adding a path with all valid files
		paths = basePath + "valid";		
		assertEquals(0, sourceParser.addPaths(paths));	
		
		// Test adding a path with all invalid files
		paths = basePath + "invalid";		
		assertEquals(1, sourceParser.addPaths(paths));	

		// Test adding a path with all both valid and invalid files
		paths = basePath + "mixed";	
		assertEquals(1, sourceParser.addPaths(paths));	
		
		// Test adding a path with valid and a path with invalid
		paths = basePath + "valid" + ":" + basePath + "invalid";
		assertEquals(1, sourceParser.addPaths(paths));
		
		// Test adding a path with valid and a path with invalid and a path that doesn't exist
		paths += ":" + basePath + "thisDirectoryDoesntExist";
		assertEquals(1, sourceParser.addPaths(paths));
		
		// Test adding with a path that doesn't exist and a recursive path
		paths = basePath + ":" + basePath + "thisDirectoryDoesntExist";
		assertEquals(2, sourceParser.addPaths(paths)); // recursive
		
		// Test adding with a path that doesn't exist and a recursive path (opposite order)
		paths = basePath + "thisDirectoryDoesntExist" + ":" + basePath;
		assertEquals(2, sourceParser.addPaths(paths)); // recursive	
	}

	@Test
	public void testAddPathsStringBoolean() {
		SourceParser sourceParser = new SourceParser();	
		
		// Test adding a single path that doesn't exist
		String paths = basePath + "thisDirectoryDoesntExist";		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));		
		
		// Test adding two paths that don't exist
		paths += ":" + basePath + "thisDirectoryAlsoDoesntExist";		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with all valid files
		paths = basePath + "valid";		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with all invalid files
		paths = basePath + "invalid";		
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));	

		// Test adding a path with all both valid and invalid files
		paths = basePath + "mixed";	
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with valid and a path with invalid
		paths = basePath + "valid" + ":" + basePath + "invalid";
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));
		
		// Test adding a path with valid and a path with invalid and a path that doesn't exist
		paths += ":" + basePath + "thisDirectoryDoesntExist";
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));
		
		// Test adding with a path that doesn't exist and a recursive path
		paths = basePath + ":" + basePath + "thisDirectoryDoesntExist";
		assertEquals(0, sourceParser.addPaths(paths, false)); // non-recursive
		assertEquals(2, sourceParser.addPaths(paths, true)); // recursive
		
		// Test adding with a path that doesn't exist and a recursive path (opposite order)
		paths = basePath + "thisDirectoryDoesntExist" + ":" + basePath;
		assertEquals(0, sourceParser.addPaths(paths, false)); // non-recursive
		assertEquals(2, sourceParser.addPaths(paths, true)); // recursive	
	}

	@Test
	public void testAddPathsListOfString() {
		SourceParser sourceParser = new SourceParser();		
		List<String> paths = new LinkedList<String>();
		
		// Test adding a single path that doesn't exist
		paths.add(basePath + "thisDirectoryDoesntExist");		
		assertEquals(0, sourceParser.addPaths(paths));	
		
		// Test adding two paths that don't exist
		paths.add(basePath + "thisDirectoryAlsoDoesntExist");		
		assertEquals(0, sourceParser.addPaths(paths));
		
		// Test adding a path with all valid files
		paths = new LinkedList<String>();
		paths.add(basePath + "valid");		
		assertEquals(0, sourceParser.addPaths(paths));	
		
		// Test adding a path with all invalid files
		paths = new LinkedList<String>();
		paths.add(basePath + "invalid");		
		assertEquals(1, sourceParser.addPaths(paths));	

		// Test adding a path with all both valid and invalid files
		paths = new LinkedList<String>();
		paths.add(basePath + "mixed");		
		assertEquals(1, sourceParser.addPaths(paths));
		
		// Test adding a path with valid and a path with invalid
		paths = new LinkedList<String>();
		paths.add(basePath + "valid");
		paths.add(basePath + "invalid");
		assertEquals(1, sourceParser.addPaths(paths));
		
		// Test adding a path with valid and a path with invalid and a path that doesn't exist
		paths.add(basePath + "thisDirectoryDoesntExist");
		assertEquals(1, sourceParser.addPaths(paths));
		
		// Test adding with a path that doesn't exist and a recursive path
		paths = new LinkedList<String>();
		paths.add(basePath);
		paths.add(basePath + "thisDirectoryDoesntExist");
		assertEquals(2, sourceParser.addPaths(paths));
		
		// Test adding with a path that doesn't exist and a recursive path (opposite order)
		paths = new LinkedList<String>();
		paths.add(basePath + "thisDirectoryDoesntExist");
		paths.add(basePath);
		assertEquals(2, sourceParser.addPaths(paths));
	}

	@Test
	public void testAddPathsListOfStringBoolean() {
		SourceParser sourceParser = new SourceParser();		
		List<String> paths = new LinkedList<String>();
		
		// Test adding a single path that doesn't exist
		paths.add(basePath + "thisDirectoryDoesntExist");		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));		
		
		// Test adding two paths that don't exist
		paths.add(basePath + "thisDirectoryAlsoDoesntExist");		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with all valid files
		paths = new LinkedList<String>();
		paths.add(basePath + "valid");		
		assertEquals(0, sourceParser.addPaths(paths, false));
		assertEquals(0, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with all invalid files
		paths = new LinkedList<String>();
		paths.add(basePath + "invalid");		
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));	

		// Test adding a path with all both valid and invalid files
		paths = new LinkedList<String>();
		paths.add(basePath + "mixed");		
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));	
		
		// Test adding a path with valid and a path with invalid
		paths = new LinkedList<String>();
		paths.add(basePath + "valid");
		paths.add(basePath + "invalid");
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));
		
		// Test adding a path with valid and a path with invalid and a path that doesn't exist
		paths.add(basePath + "thisDirectoryDoesntExist");
		assertEquals(1, sourceParser.addPaths(paths, false));
		assertEquals(1, sourceParser.addPaths(paths, true));
		
		// Test adding with a path that doesn't exist and a recursive path
		paths = new LinkedList<String>();
		paths.add(basePath);
		paths.add(basePath + "thisDirectoryDoesntExist");
		assertEquals(0, sourceParser.addPaths(paths, false)); // non-recursive
		assertEquals(2, sourceParser.addPaths(paths, true)); // recursive
		
		// Test adding with a path that doesn't exist and a recursive path (opposite order)
		paths = new LinkedList<String>();
		paths.add(basePath + "thisDirectoryDoesntExist");
		paths.add(basePath);
		assertEquals(0, sourceParser.addPaths(paths, false)); // non-recursive
		assertEquals(2, sourceParser.addPaths(paths, true)); // recursive		
	}

	@Test
	public void testAddPathString() {
		SourceParser sourceParser = new SourceParser();
		
		// Test adding a path that doesn't exist
		assertEquals(0, sourceParser.addPath(basePath + "thisDirectoryDoesntExist"));
		
		// Test adding a path with all valid files
		assertEquals(0, sourceParser.addPath(basePath + "valid"));
		
		// Test adding a path with all invalid files
		assertEquals(1, sourceParser.addPath(basePath + "invalid"));
		
		// Test adding a path with a mix of valid and invalid files
		assertEquals(1, sourceParser.addPath(basePath + "mixed"));
		
		// Test adding a path with subdirectories of a mix of valid and invalid files
		assertEquals(2, sourceParser.addPath(basePath)); 
	}

	@Test
	public void testAddPathStringBoolean() {
		SourceParser sourceParser = new SourceParser();
		
		// Test adding a path that doesn't exist
		assertEquals(0, sourceParser.addPath(basePath + "thisDirectoryDoesntExist", false));
		assertEquals(0, sourceParser.addPath(basePath + "thisDirectoryDoesntExist", true));
		
		// Test adding a path with all valid files
		assertEquals(0, sourceParser.addPath(basePath + "valid", false));
		assertEquals(0, sourceParser.addPath(basePath + "valid", true));
		
		// Test adding a path with all invalid files
		assertEquals(1, sourceParser.addPath(basePath + "invalid", false));
		assertEquals(1, sourceParser.addPath(basePath + "invalid", true));
		
		// Test adding a path with a mix of valid and invalid files
		assertEquals(1, sourceParser.addPath(basePath + "mixed", false));
		assertEquals(1, sourceParser.addPath(basePath + "mixed", true));
		
		// Test adding a path with subdirectories of a mix of valid and invalid files
		assertEquals(0, sourceParser.addPath(basePath, false)); // Non-recursive
		assertEquals(2, sourceParser.addPath(basePath, true)); // Recursive
	}

	@Test
	public void testAddFile() {
		SourceParser sourceParser = new SourceParser();
		
		// Test adding a file that doesn't exist
		assertEquals(1, sourceParser.addFile(basePath + "thisFileDoesntExist"));
		
		// Test adding a file that does exist but is not valid
		assertEquals(1, sourceParser.addFile(basePath + "mixed/InvalidJavaFile.java"));
		
		// Test adding a file that does exist and is valid
		assertEquals(0, sourceParser.addFile(basePath + "mixed/ValidJavaFile.java"));
		
		// Test re-adding a file that exists
		assertEquals(0, sourceParser.addFile(basePath + "mixed/ValidJavaFile.java"));
	}

	@Test
	public void testGetVariables() {
		SourceParser sourceParser = new SourceParser();
		
		String file = basePath + "valid/ValidJavaFile.java";
		assertEquals(0, sourceParser.addFile(file));
		
		// Try all the lines that have variables
		List<String> variables = sourceParser.getVariables(file, 8); // Line 8
		assertEquals(1, variables.size());
		assertTrue(variables.contains("a"));
				
		variables = sourceParser.getVariables(file, 10); // Line 10
		assertEquals(2, variables.size());
		assertTrue(variables.contains("b"));
		assertTrue(variables.contains("c"));		
		
		variables = sourceParser.getVariables(file, 12); // Line 12
		assertEquals(3, variables.size());
		assertTrue(variables.contains("a"));
		assertTrue(variables.contains("b"));
		assertTrue(variables.contains("c"));
		
		// Try a few lines that don't have variables
		variables = sourceParser.getVariables(file, 7);
		assertNull(variables);		
		variables = sourceParser.getVariables(file, 9);
		assertNull(variables);		
		variables = sourceParser.getVariables(file, 17); // One past range of file
		assertNull(variables);		
		variables = sourceParser.getVariables(file, 700); // Way outside range of file
		assertNull(variables);		
		
		// Try a file that wasn't loaded (lots of lines, just in case)
		for (int i = 0; i < 1024; i++) {
			variables = sourceParser.getVariables(basePath + "valid/ThisFileIsntLoaded.java", i);
			assertNull(variables);	
		}
	}

}
