/* 
 * Client.java
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Objects;

public class Client 
{
	public static final int SERVER_PORT = 9942;

	public static void main(String[] args) 
	{
		Socket clientSocket = null;  
		PrintStream os = null;
		BufferedReader is = null;
		String userInput = null;
		BufferedReader stdInput = null;
		String clientIp = "";

		//Check the number of command line parameters
		if (args.length < 1)
		{
			System.out.println("Please enter a valid IP address or some loopback address to connect to the server.");
			Scanner scanner = new Scanner( System.in );
			clientIp = scanner.nextLine();
		}

		// Try to open a socket on SERVER_PORT
		// Try to open input and output streams
		try 
		{
			clientSocket = new Socket(clientIp, SERVER_PORT);
			System.out.println("connection to server success!");
			os = new PrintStream(clientSocket.getOutputStream());
			is = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
			stdInput = new BufferedReader(new InputStreamReader(System.in));
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Don't know about host: hostname");
		} 
		catch (IOException e) 
		{
			System.err.println("Couldn't get I/O for the connection to: hostname");
		}

		// If everything has been initialized then we want to write some data
		// to the socket we have opened a connection to on port 25
		if (clientSocket != null && os != null) 
		{
			try 
			{
				//Start a child thread to handle the server's messages
				SThread sThread = new SThread(clientSocket);
				sThread.start();
				while ((userInput = stdInput.readLine())!= null)
				{
					//Client to server input handled here
					os.println(userInput);
				}
				os.close();
				clientSocket.close();  
				System.exit(0);
			} 
			catch (IOException e) 
			{
				//Error Handler Here If needed
			} 
		}
	}           
}

/*
 * SThread Class, which handle the server's messages
 */
class SThread extends Thread 
{
	Socket socket;
	BufferedReader is = null;
	String serverInput = null;

	/**
	 * Constructor
	 */
	SThread(Socket socket)
	{
		this.socket = socket;
	}

	/*
	 * Child thread of execution, handle the server's messages
	 */
	public void run()
	{
		try 
		{
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while ((serverInput = is.readLine())!= null)
			{
				if (Objects.equals(serverInput.substring(0, 3), "210"))
				{
					System.out.println(serverInput);
					break;
				}
				else if (Objects.equals(serverInput.substring(0, 3), "200"))
				{
					System.out.println(serverInput.substring(0, 6));

					if (serverInput.length() > 22)
					{
						String [] list = serverInput.substring(6).split("@");
						for (int i = 0; i < list.length; i++)
						{
							System.out.println(list[i]);
						}
					}
					else if (Objects.equals(serverInput.substring(6), "QUIT"))
					{
						System.out.println(serverInput);
						break;
					}
					else
					{
						//Do nothing
					}
				}
				else
				{
					System.out.println(serverInput);
				}
			}
			is.close();
			socket.close();
			System.exit(0);
		} 
		catch (IOException e) 
		{
			//Error Handler
		}
	}           
}

