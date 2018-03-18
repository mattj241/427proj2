CIS 427 - Winter 2018 - Program 2

Group: Matthew London , Daniil Shmerko

Responsibilities:
All work was very evenly split. 
 - Daniil implemented a portion of the functions and sufficient error handling
 - Matthew implemented the other portion of commands, error handling, file read/writing, and this README.

Implemented Commands:
ADD - format: add <first name> <last name> <phone # (ex. 734-880-9921)>
DELETE - format: delete <log ID>
LIST - format: list
QUIT - format: quit
SHUTDOWN - format: shutdown
LOGIN - format: login <userName> <password>
LOGOUT - format: logout
WHO - format: who
LOOK - format: look <search parameter 1-3> <search term>

How to compile and run this project:
1) You must have AT LEAST 2 instances of the UNIX terminal windows under the UMD servers. 1 for the server, and however many for your clients. 
  Unpack the .tar file in a destination you please, all the files should remain in the same directory.

2) How to compile: In 1 UNIX terminal, type exactly whats in the quotes: "javac Server.java",  "javac MultiThreadServer.java" and then "javac Client.java". All files should now be compiled for use.

3) In the server terminal, type "java Server" to start running the server. "Server started!" should be shown in the server terminal.

4) In any client terminal, type "java Client" to initiate a connection to the running server. 
   ***"Please enter a valid IP address or some loopback address to connect to the server." should appear in the client terminal.
   Type "127.0.0.1" for a loopback client, and use your system's IP (if testing on UMD servers, I have had luck with: 141.215.69.204) address for another terminal client. 
   Press enter in each client terminal to initiate connection.
   If done correctly, a response from both the client and server will appear to verify a successful connection.

5) Go ahead and test any commands.

***You should be able to just send an argument with your IP address as well, but we encourage you to do it this way as it keeps things simple.

Known Bugs:
N/A, hopefully you don't prove us wrong!

Sample Client (and server) runs:
ADD	
 - input: add Matt London 734-000-1010
 - client: 200 OK
	   The new record is: 1001
 - server: 200 OK Adding listing to log...

DELETE
 - input: delete 1000
 - client: 403 The Record ID does not exist.
 - server: ID not found.

 - input: delete 1001
 - client: 200 OK
 - server: 200 OK Removing listing from log...

LIST
 - input: list
 - client: Empty Log!
 - server: 200 OK Listing log to client...

 - input: list
 - client: (Sample log book!)
	1002 Matt London 111-111-1111
	1003 Jin Guo 111-111-1112
	1004 Mo Van 111-111-1113
	1005 Bruce Elenbogen 111-111-1114
	1006 Hello World 111-111-1115
 - server: 200 OK Listing log to client...

QUIT
 - input: quit
 - client: 200 OK
 - server: 200 OK connection from <the user if they were logged in, else blank> removed.
	   user <the user if they were logged in, else blank> is now logged out

SHUTDOWN
 - input: shutdown (if root)
 - client: (ALL CLIENTS!) 210 the server is about to shutdown...
 - server: 200 OK SHUTDOWN
	   Shutting down...writing log to memory file.

 - input: shutdown (if NOT root)
 - client: 402 User not allowed to execute this command
 - server: Access denied

LOGIN:
 - input: login mary mary01
 - client: 200 OK
 - server: LOGIN mary mary01 200 OK

 - input: login mary mary1111111
 - client: 410 wrong username or password
 - server: LOGIN mary mary1111111
	   Login info not found

LOGOUT:
 - input: logout (no user logged in, system doesn't care)
 - client: 200 OK
	   You have beem logged out.
 - server: 200 OK LOGOUT

 - input: logout (mary logged in)
 - client: 200 OK
	   You have beem logged out.
 - server: 200 OK LOGOUT
           user mary is now logged out.

WHO:
 - input: who (no one logged in)
 - client: 200 OK
	   The list of active users:
	   No one is online.
 - server: listing current users...

 - input: who (mary is online)
 - client: 200 OK
	   The list of active users:
	   mary  127.0.0.1
 - server: listing current users...

LOOK: (all below results will be based on the log file I submit with the .tar file)
 - input: look 1 Morgan
 - client: Found 2 matches:
           1004 Morgan Freeman 123-445-1231 
           1007 Morgan VanOtteren 999-999-9999 
 - server: LOOK 1 Morgan 
           200 OK Sending matches to client...

 - input:  look 3 112-221-2222
 - client: 404 Your search did not match any records
 - server: LOOK 3 112-221-2222 
	   No matches
