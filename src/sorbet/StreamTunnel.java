package sorbet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamTunnel implements Runnable {
	private InputStreamReader in;
	private OutputStreamWriter out;
	
	public StreamTunnel(InputStream in, OutputStream out) {
		this.in = new InputStreamReader(in);
		this.out = new OutputStreamWriter(out);
	}

	@Override
	public void run() {
		try {
			int readItem = in.read();
			
			while (readItem != -1) {
				out.write(readItem);
				
				readItem = in.read();
			}
		}
		catch (IOException e) {
			System.out.println("Error: Could not read/write stream: " + e.getMessage());
		}
		
		try {
			in.close();
			out.close();
		}
		catch (IOException e) {
			System.out.println("Error: Could not close stream: " + e.getMessage());
		}
	}
}
