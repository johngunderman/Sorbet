package functional;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FuncTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();                                                                                                 
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent));                                                                                                                           
		System.setErr(new PrintStream(errContent));  
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(null);                                                                                                                                                  
		System.setErr(null);
	}

	@Test
	public final void testMain() {
		sorbet.Main.main(new String[] {"--main=sorbet.Main --src=../src"});
		
		File f = new File("testout.txt");
		FileReader fr;
		try {
			fr = new FileReader(f);
	
			assertEquals(fr.toString(), outContent.toString());
			assertEquals("", errContent.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
