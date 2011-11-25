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
	
	private static final String rootPath = "test/sourceparser/testfiles/";

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
	public void testAddRootPaths() {
		SourceParser sourceParser;
		
		// Test a path that exists with only valid files
		sourceParser = new SourceParser();
		assertEquals(0, sourceParser.addRootPaths(rootPath + "valid"));		
		// Test adding it twice
		assertEquals(1, sourceParser.addRootPaths(rootPath + "valid"));
		
		// Test adding a path that exists with only invalid files
		sourceParser = new SourceParser();
		assertEquals(1, sourceParser.addRootPaths(rootPath + "invalid"));		
		// Test adding it twice
		assertEquals(1, sourceParser.addRootPaths(rootPath + "invalid"));
		
		// Test adding a path that contains both files
		sourceParser = new SourceParser();
		assertEquals(1, sourceParser.addRootPaths(rootPath + "mixed"));		
		// Test adding it twice
		assertEquals(2, sourceParser.addRootPaths(rootPath + "mixed"));
		
		// Test adding a path that contains the above three folders
		sourceParser = new SourceParser();
		assertEquals(2, sourceParser.addRootPaths(rootPath));		
		// Test adding it twice
		assertEquals(4, sourceParser.addRootPaths(rootPath));
	}

	@Test
	public void testGetVariables() {
		SourceParser sourceParser = new SourceParser();
		
		String file = "valid/ValidJavaFile.java";
		assertEquals(2, sourceParser.addRootPaths(rootPath));
		
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
			variables = sourceParser.getVariables(rootPath + "valid/ThisFileIsntLoaded.java", i);
			assertNull(variables);	
		}
	}

}
