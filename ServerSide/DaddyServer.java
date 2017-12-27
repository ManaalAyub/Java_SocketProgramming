/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming.ServerSide;

import java.awt.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import static socketprogramming.ServerSide.ImageServer.serverSocket;

/**
 *
 * @author manal
 */
public class DaddyServer {
    //These need to be acquired from the sender address and port of udp packet(file) of each sub-server.

        private static int MAXCONNECTIONS=0; 
        private static int DaddyServer_PORT=1000;

        private static int ImageServer_PORT;
        private static int AudioServer_PORT;
        private static int VideoServer_PORT;
        
        private static String ImageServer_IP;
        private static String AudioServer_IP;
        private static String VideoServer_IP;

        private static ArrayList<Byte[]> imageInfo; 
        private static ArrayList<Byte[]> audioInfo;
        private static ArrayList<Byte[]> videoInfo;
        //All the images,videos and audios need to be stored in the form of bytes here.

        
      
//make a constructor that assigns a port number and creates a socket for udp 
//collection of images.
//collection of audios.
//collection of videos.
        
        
//The basic flow of logic in a server that supports multiple clients is this:
//while (true) {
//    accept a connection;
//    create a thread to deal with the client; }

//The first thread i.e. Daddy Server acts as the main server.
        
 public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, Exception 
    { 
            //DATAGRAM PACKET FROM IMAGE SUB_SERVER.
            byte b[]=new byte[3072];
            DatagramSocket dsoc=new DatagramSocket(DaddyServer_PORT);
            //FileOutputStream f=new FileOutputStream("C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/recvdImageInfo.txt");
            //Test_name, file_size, test_type, sub-server IP, sub-server-port
            //DATAGRAM PACKET FROM CLIENT. 
            
            int choice=2;//-------------------------------------> Change this to change SEND category.
            
            int i=0;
            String subServerInfo=ImageServer_PORT +":"+ "localhost";; 
            //Lets set image question as default. //This will be overwritten.
            DatagramSocket serverSocketForUDP = new DatagramSocket(9876);
            byte[] receiveData = new byte[100];
            byte[] sendData = new byte[100];
            while(true)
            {
                  //Getting File From Image Server.
                  System.out.println("Daddy Server Started"); 
                  DatagramPacket dp=new DatagramPacket(b,b.length);
                  dsoc.receive(dp);
                  ImageServer_PORT=dp.getPort();   //We get Image_Sub-server's port.
                  ImageServer_IP=dp.getAddress().toString();
                  System.out.println("Port_Image_Server: "+ImageServer_PORT);
                  System.out.println("IP_Image_Server: "+ImageServer_IP);
                  System.out.println(new String(dp.getData(),0,dp.getLength())); 
                  
                  //Getting File From Audio Server.
                  DatagramPacket dp1=new DatagramPacket(b,b.length);
                  dsoc.receive(dp1);
                  AudioServer_PORT=dp1.getPort();   //We get Audio_Sub-server's port.
                  AudioServer_IP=dp1.getAddress().toString();
                  System.out.println("Port_Audio_Server: "+AudioServer_PORT);
                  System.out.println("IP_Audio_Server: "+AudioServer_IP);
                  System.out.println(new String(dp1.getData(),0,dp1.getLength())); 
                  
                  //Getting File From Video Server.
                  DatagramPacket dp2=new DatagramPacket(b,b.length);
                  dsoc.receive(dp2);
                  VideoServer_PORT=dp2.getPort();   //We get Video_Sub-server's port.
                  VideoServer_IP=dp2.getAddress().toString();
                  System.out.println("Port_Video_Server: "+VideoServer_PORT);
                  System.out.println("IP_Video_Server: "+VideoServer_IP);
                  System.out.println(new String(dp2.getData(),0,dp2.getLength()));  //Need to store all this information in a List.
                  
                  //Getting Preference From Client.
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocketForUDP.receive(receivePacket);
                  String sentence = new String( receivePacket.getData());
                  //int choice=Integer.valueOf(sentence);
                  System.out.println("RECEIVED CLIENT PREFERENCE: " + sentence);
                  
                  String[] recvd = sentence.split(":");
                  String ch = recvd[1]; 
                  System.out.println("Category CHOICE : "+ch);
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
            if(choice==1)
                    subServerInfo = ImageServer_PORT +":"+ "localhost"; //InetAddress.getByName("localhost"); 
            else if(choice==2)
                    subServerInfo = AudioServer_PORT +":"+ "localhost";
            else if(choice==3)
                    subServerInfo = VideoServer_PORT +":"+ "localhost";
                  sendData = subServerInfo.getBytes();
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocketForUDP.send(sendPacket);
            
                  try{
            if(choice==1)
                              serverSocket = new ServerSocket(ImageServer_PORT);
            else if(choice==2)
                              serverSocket= new ServerSocket(AudioServer_PORT);
            else if(choice==3)
                              serverSocket= new ServerSocket(VideoServer_PORT);
                        Socket clientCommSocket=null; //Client-Communication-Socket.
                        while((i++ < MAXCONNECTIONS) || (MAXCONNECTIONS == 0)) 
                         {
            if(choice==1){

                                  clientCommSocket = serverSocket.accept();
                                  System.out.println(clientCommSocket); 
                                  System.out.println("Client "+i+"  Connected"); 
                                  ImageServer socketForImages=new ImageServer(clientCommSocket);
                                  Thread imageServerThread = new Thread(socketForImages); 
                                  imageServerThread.start();
                             }
            else if(choice==2)
                             {

                                  clientCommSocket = serverSocket.accept();
                                  System.out.println(clientCommSocket); 
                                  System.out.println("Client "+i+"  Connected"); 
                                  AudioServer socketForAudio=new AudioServer(clientCommSocket);
                                  Thread audioServerThread = new Thread(socketForAudio); 
                                  audioServerThread.start(); 
                             }
            else if(choice==3)
                             { 
        //1. check this.  3. UI 4. Lists btw threads. 
        //5. Files in UDP. 6.videoIO and audioIO in java 7. Local IP Address for working on different PC's. 
                                  clientCommSocket = serverSocket.accept();
                                  System.out.println(clientCommSocket); 
                                  System.out.println("Client "+i+"  Connected"); 
                                  VideoServer socketForVideo=new VideoServer(clientCommSocket);
                                  Thread videoServerThread = new Thread(socketForVideo); 
                                  videoServerThread.start();

                             }
                         } 
                   }
                   catch (IOException ioe) { 
                    System.out.println("IOException on socket listen: " + ioe); 
                    ioe.printStackTrace(); 
                  } 
            }
    }    
}
