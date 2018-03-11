/* 
 * ChildThread.java
 */


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;


public class ChildThread extends Thread 
{
    static  Vector<ChildThread> handlers = new Vector<ChildThread>(20);
    private Socket socket;
    private BufferedReader in;
    private BufferedReader is;
    private PrintWriter out;
    private PrintStream os;
    
    private static int recordToBeSet; /*define from file*/;
	private static String listingString = "";
	private static String serverFile = "server_info.txt"; //Default path of the database
	static ArrayList<String[]> infoLog = new ArrayList<String[]>();
    
  //Writes all of the data from the array to the text file database
	private static void writeToFile()
	{
		try {
			PrintWriter eraser = new PrintWriter(serverFile);
			eraser.println("");
			eraser.close();

			PrintWriter writer = new PrintWriter(serverFile);

			writer.println(recordToBeSet);
			for(int i = 0; i < infoLog.size(); i++)
			{
				for(int j = 0; j < 4; j++)
				{
					if (j != 3)
					{
						writer.print(infoLog.get(i)[j] + "@");
					}
					else
					{
						writer.println(infoLog.get(i)[j]);
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This function takes in initial user input then parses it
		//To find out what type of command to the server it is
		//The command type is then returned
		public static int processInput(String [] inputArray)
		{
			if(Objects.equals(inputArray[0], "ADD"))
			{
				if (inputArray.length != 4)
				{
					return 301;
				}
				else if (inputArray[3].matches("\\d{3}-\\d{3}-\\d{4}"))
				{
					return 1;
				}
				else
				{
					return 301;
				}
			}
			else if(Objects.equals(inputArray[0], "DELETE"))
			{
				if (inputArray.length != 2)
				{
					return 301;
				}
				return 2;
			}
			else if(Objects.equals(inputArray[0], "LIST"))
			{
				if (inputArray.length != 1)
				{
					return 301;
				}
				return 3;
			}
			else if(Objects.equals(inputArray[0], "QUIT"))
			{
				if (inputArray.length != 1)
				{
					return 301;
				}
				return 4;
			}
			else if(Objects.equals(inputArray[0], "SHUTDOWN")) {
				if (inputArray.length != 1) {
					return 301;
				}
				return 5;
			}
			else 
			{
				return 300; //Returned if the command is not one of the five
			}
		}

		
		//This function takes the command type and the user input as arguments
		//The function then processes the input depending on the command type
		public static String executeCommand(int inputNum, String[] inputArray)
		{
			String idToCheck = "";
			String message_OK = "200 OK";
			String message_NotFound = "403 The Record ID does not exist.";
			String newRecord = "The new Record is: ";
			
			if (inputNum == 1)
			{
				for (int i = 0; i < inputArray.length; i++)
				{
					System.out.print(inputArray[i] + " ");
				}
				if (recordToBeSet == 0)
				{
					recordToBeSet = 1001;
				}
				String [] bufferArray = {"0", "0", "0", "0"};
				bufferArray[0] = String.valueOf(recordToBeSet);
				recordToBeSet++;

				for (int i = 1; i < 4; i++)
				{
					bufferArray[i] = inputArray[i];
				}
				infoLog.add(bufferArray);
				System.out.println("\n" + message_OK + " Add listing to log...");
				return message_OK + newRecord + bufferArray[0];
			}
			else if (inputNum == 2)
			{
				boolean found = false;

				idToCheck = inputArray[1];
				for (int i = 0; i < inputArray.length; i++)
				{
					System.out.print(inputArray[i] + " ");
				}
				for (int i = 0; i < infoLog.size() && !found; i++)
				{
					if (Objects.equals(idToCheck, infoLog.get(i)[0]))
					{
						found = true;
						infoLog.remove(i);
					}
				}
				if (found)
				{
					System.out.println("\n" + message_OK + " Removing listing in log...");
					return message_OK;
				}else {
					System.out.println("\nID not found");
					return message_NotFound;
				}
			}
			else if (inputNum == 3)
			{
				listingString = "";
				if (infoLog.isEmpty() || infoLog.size() == 0)
				{
					listingString = "Empty Log!";
				}
				else
				{
					for (int i = 0; i < infoLog.size(); i++)
					{
						for (int j = 0; j < 4; j++)
						{
							listingString = listingString + infoLog.get(i)[j] + " ";
							if (j == 3)
							{
								listingString += "@";
							}
						}
					}
				}
				System.out.println(message_OK + " Listing log to client...");
				return message_OK + listingString;
			}
			else if (inputNum == 4)
			{
				System.out.println(message_OK + " Client connection removed.");
				return message_OK + "QUIT";
			}
			else
			{
				System.out.println(message_OK + " SHUTDOWN");
				System.out.println("Shutting down...Writing Log memory to file");
				writeToFile(); //Writes all of the data to file upon shutting down
				return message_OK + "SHUTDOWN";
			}
		}


    public ChildThread(Socket socket) throws IOException 
    {
		this.socket = socket;
		in = new BufferedReader(
		    new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(
		    new OutputStreamWriter(socket.getOutputStream()));
		os = new PrintStream(socket.getOutputStream());
		
		
		try {
			File fileExists = new File(serverFile);
			fileExists.createNewFile(); //Automatically creates new file if non existent

			Scanner fileScanner = new Scanner(new File(serverFile));
			
			
			if (serverFile.length() == 0)
			{
				//Parse the file
				while(fileScanner.hasNextLine())
				{
					String text = fileScanner.nextLine();
					if (text.matches("\\d{4}$"))
					{
						recordToBeSet = Integer.parseInt(text);
					}
					else
					{
						String [] infoLogLine = text.split("@");
						infoLog.add(infoLogLine);
					}
				}
			}
			else
			{
				recordToBeSet = 1001;
			}
			fileScanner.close();     
		}
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open '" + 
							serverFile + "'");                
		}

    }

    public void run() 
    {
		String line = "";
		boolean done = false;
		int typeCommand = 0;
		String sendToClient = "";
		
		synchronized(handlers) 
		{
		    // add the new client in Vector class
		    handlers.addElement(this);
		}
		
		try 
		{
		    while ((line = in.readLine()) != null) 
		    {
				String[] organizedInput = line.split(" ");
				organizedInput[0] = organizedInput[0].toUpperCase();
				if (Objects.equals(organizedInput[0], "SHUTDOWN"))
				{
					done = true;
				}
				typeCommand = processInput(organizedInput);
				if(typeCommand == 300)
				{
					os.println("300 invalid command");
				}
				else if(typeCommand == 301)
				{
					os.println("301 invalid message format");
				}
				else
				{
					sendToClient = executeCommand(typeCommand, organizedInput);
					os.println(sendToClient);
				}
				
				/*// Broadcast it to everyone!  You will change this.  
				// Most commands do not need to broadcast
				for(int i = 0; i < handlers.size(); i++) 
				{	
				    synchronized(handlers) 
				    {
						ChildThread handler = (ChildThread)handlers.elementAt(i);
						if (handler != this) 
						{
						    handler.out.println(line);
						    handler.out.flush();
						}
				    }
				}*/
		    }
		} 
		catch(IOException ioe) 
		{
		    ioe.printStackTrace();
		} 
		finally 
		{
		    try 
		    {
				in.close();
				out.close();
				socket.close();
		    } 
		    catch(IOException ioe) 
		    {
		    	
		    } 
		    finally 
		    {
				synchronized(handlers) 
				{
				    handlers.removeElement(this);
				}
		    }
		}
    }
}

