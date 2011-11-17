package sorbet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamTunnel {
	private StreamTunnelRunnable tunnel;
	private Thread thread;
	
	public StreamTunnel(InputStream in, OutputStream out) {
		tunnel = new StreamTunnelRunnable(in, out);
		
		thread = new Thread(tunnel);
		thread.start();
	}
	
	private class StreamTunnelRunnable implements Runnable {
		private InputStreamReader in;
		private OutputStreamWriter out;
		
		public StreamTunnelRunnable(InputStream in, OutputStream out) {
			this.in = new InputStreamReader(in);
			this.out = new OutputStreamWriter(out);
		}

		public void run() {
			try {
				int readItem = in.read();
				
				while (readItem != -1) {
					out.write(readItem);
					
					readItem = in.read();
				}
			}
			catch (IOException e) {
				System.err.println("Error: Could not read/write stream: " + e.getMessage());
			}
			
			try {
				in.close();
				out.close();
			}
			catch (IOException e) {
				System.err.println("Error: Could not close stream: " + e.getMessage());
			}
		}
	}
}
