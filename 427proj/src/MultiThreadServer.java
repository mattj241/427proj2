

/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadServer {

	
	public static final int SERVER_PORT = 9942; //Port for the client
	
    public static void main(String args[]) 
    {
	ServerSocket myServerice = null;
	Socket serviceSocket = null;

	// Try to open a server socket 
	try {
	    myServerice = new ServerSocket(SERVER_PORT);
	}
	catch (IOException e) {
	    System.out.println(e);
	}   

	// Create a socket object from the ServerSocket to listen and accept connections.
	while (true)
	{
	    try 
	    {
			// Received a connection
			serviceSocket = myServerice.accept();
			System.out.println("MultiThreadServer: new connection from " + serviceSocket.getInetAddress());
	
			// Create and start the client handler thread
			ChildThread cThread = new ChildThread(serviceSocket);
			cThread.start();
			cThread.join();
	    }   
	    catch (IOException e) 
	    {
	    	System.out.println(e);
	    } catch (InterruptedException e) {
			System.out.println("something happen when joining a thread");
			e.printStackTrace();
		}
	}
    }
}

