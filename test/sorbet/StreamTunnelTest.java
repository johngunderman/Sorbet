package sorbet;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StreamTunnelTest {

	@Test
	public void testStreamTunnel() {		
		// Create the random data to test with
		byte[] inputData = new byte[1024];
		for (int i = 0; i < inputData.length - 2; i++) {
			inputData[i] = (byte)(Math.random() * Byte.MAX_VALUE);
		}
		inputData[1022] = 0;
		inputData[1023] = Byte.MAX_VALUE;
		
		// Create our input and output streams
		ByteArrayInputStream in = new ByteArrayInputStream(inputData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Create the StreamTunnel to test
		StreamTunnel tunnel = new StreamTunnel(in, out);
		
		// Wait a bit just to make sure the tunnel thread has time to process
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		
		// Get the output data
		byte[] outputData = out.toByteArray();
			
		// Check to make sure they're equal
		assertArrayEquals(outputData, inputData);
	}

}
