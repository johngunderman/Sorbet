package log;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import mockjdi.MockType;
import mockjdi.MockValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jdi.Value;

public class PrintLoggerTest {
	
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
	public final void testLog() {
		PrintLogger p = new PrintLogger();
		p.log("foo.java", 45, "main", "b", new MockValue(new MockType("int"), "32"));
		assertEquals("foo.java:45 in main(), [int] b = 32\n", outContent.toString());
		assertEquals("", errContent.toString());

	}

}
