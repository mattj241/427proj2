
/* 
 * Client.java
 */

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

public class Client 
{
	public static final int SERVER_PORT = 9942;

	public static void main(String[] args) 
	{
		Socket clientSocket = null;  
		PrintStream os = null;
		BufferedReader is = null;
		String userInput = null;
		String serverInput = null;
		BufferedReader stdInput = null;
		String serverIp = "";
		
		//Check the number of command line parameters
		//Ask for ip address if it is not passed as an argument
		if (args.length < 1)
		{
			System.out.println("Usage: <Server IP Address>");
		}
		
		//Get ip address from user
	    Scanner scanner = new Scanner( System.in );
	    serverIp = scanner.nextLine();
	      
		// Try to open a socket on SERVER_PORT
		// Try to open input and output streams
		try 
		{
			clientSocket = new Socket(serverIp, SERVER_PORT);
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

		System.out.println("Connected to Host!");

		if (clientSocket != null && os != null && is != null) 
		{
			try 
			{
				//Start a child thread to handle the server's messages
				SThread sThread = new SThread(clientSocket);
				sThread.start();
				
				while ((userInput = stdInput.readLine())!= "SHUTDOWN")
				{
					os.println(userInput);
					serverInput = is.readLine();
					if (Objects.equals(serverInput.substring(0, 1), "2"))
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
						else if (Objects.equals(serverInput.substring(6), "QUIT")
								|| Objects.equals(serverInput.substring(6), "SHUTDOWN"))
						{
							break;
						}
						else
						{
							System.out.println(serverInput.substring(6));
						}
					}
					else
					{
						System.out.println(serverInput);
					}
				}

				// close the input and output stream
				// close the socket
				os.close();
				is.close();
				clientSocket.close();   
			} 
			catch (IOException e) 
			{
				System.err.println("IOException:  " + e);
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
		System.out.println("s:" + serverInput);
	    }

	    is.close();
	    socket.close();   
	    System.exit(0);
	} 
	catch (IOException e) 
	{
	}
    }           
}


