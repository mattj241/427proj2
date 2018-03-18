/*
 * Server.java
 */

import java.io.*;
import java.net.*;

public class MultiThreadServer {

	public static final int SERVER_PORT = 9942; //Port for the client

	public static void main(String args[]) throws IOException, InterruptedException 
	{
		ServerSocket myServerice = null;
		Socket serviceSocket = null;
		boolean shutdown = false;
		int timer = 0;

		// Try to open a server socket 
		try 
		{
			myServerice = new ServerSocket(SERVER_PORT);
			System.out.println("Server started!");
		}
		catch (IOException e) {
			System.out.println(e);
		}   

		// Create a socket object from the ServerSocket to listen and accept connections.
		try
		{
			while (true)
			{
				// Received a connection
				serviceSocket = myServerice.accept();
				System.out.println("MultiThreadServer: new connection from " + serviceSocket.getInetAddress());

				// Create and start the client handler thread
				ChildThread cThread = new ChildThread(serviceSocket);
				cThread.start();
			}
		}
		catch (IOException e) 
		{
			System.out.println(e);
		} 
		finally
		{
			myServerice.close();
			System.exit(0);
		}
	}
}

