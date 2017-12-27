# Java_SocketProgramming

Following is the question statement for this particular project:

You have to design an Online Test System which contains three types of Data
•	Audio.
•	Video.
•	Images.
The test consists on 3 portions.
•	Image Test (image containing questions and Textual Answers).
•	Audio Test (Audio questions and Textual Answers).
•	Video Test (Video questions and Textual Answer)

Details:
Whenever a client connects to the server, it will display three kinds of tests (image, audio and video). The main server does not want to overburden itself by listening to all incoming clients and handle the tests, so it only stores the info about which sub-server contains what sort of test. When a sub-server connects to the main server it sends the main server a file containing the list of subjects of tests it has. When a client connects to a server it has to choose the type of test and subject of test. The main server searches the records and sends back the appropriate test.

You have to:
1.	Design a Main Server in C/Java.
2.	Design a Client with Graphical User Interface. You can do it in any language you want.
3.	Design a TCP Sub-server. When a sub-server connects to the main server it sends it the list of the tests it has using UDP. The format of the file sub-server has is:
Subject of the test, type of test, size 
The list a sub-server send to the main server will have the following format:
Test_name, file_size, test_type, sub-server IP, sub-server-port
There should be at least 3 instances of sub-server
4.	Main server will make a database with all the data it gets from the sub-servers.
5.	Client will send request to this server through this GUI using UDP.
E.g. Client will send “Mathematics & Written test”.
6.	Main server will search the record in the database it has and will send these search results to the client, along with the IP and port number of the sub-server to which that respective search result belongs to. (You can select the format of server’s message as per your ease!)
7.	The Client will show these results in GUI. It will not show the IP and Port of the sub-server as it is for internal use.
8.	The client will select a search result of its own choice and develop a tcp connection with the sub server to which that search result belongs to and start downloading the file or streaming the audio/video (Don’t forget that IP and Port number were sent by the server to the client earlier!).

9.	Since the file can be too large it has to be broken down into chunks. 
10.	The client will receive those chunks and upon receiving. Adding a seek bar at the client’s end will earn your bonus marks. This code should work for at least three clients accessing the server simultaneously.
11.	Client will play the audio/video or show written test and user will send the answer to server. Server will store this answer along with subject name and question number and client id.
