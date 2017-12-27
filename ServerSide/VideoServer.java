/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming.ServerSide;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author manal
 */
public class VideoServer  extends Thread {
    static ServerSocket serverSocket; 
    Socket server; 
    public static BufferedImage bimg; 
    DataInputStream din;
    DataOutputStream dout;
    public final static String FILE_TO_SEND = "C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Videos/source.avi";    
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;

    public VideoServer(Socket clientSocket) throws IOException, SQLException, ClassNotFoundException, Exception 
    { 
        System.out.println("I am VIDEO Server.");
        this.server=clientSocket;
        
    } 

    public void run() 
    { 
        while(true) 
        { 
            try 
            { 
                //To avoid writing or reading from a closed socket's stream, the following check is used.
                if(server!=null && server.isClosed()==false){
                        System.out.println("Communicating With Client.");
                        din=new DataInputStream(server.getInputStream()); 
                        dout=new DataOutputStream(server.getOutputStream());
                      

                        //1.Write.
                        dout.writeUTF("Server said: Hello Client :) "); 
                        //2.Read.
                        System.out.println("Client said: "+ din.readUTF()); 

    //                JFrame frame = new JFrame();
    //                frame.getContentPane().add(new JLabel(new ImageIcon(bimg)));
    //                frame.pack();
    //                frame.setVisible(true);
                    //Writes to Socket.
                      System.out.println("Sending Video.");
                    try {

                        //image read as buffered image. encoded to string. String decoded to byte array. 
                        //This array written to socket output stream.  
                        //Following Code: Saves Image in Bytes and then makes an image from them.
                        
                        File myFile = new File (FILE_TO_SEND);
                        byte [] mybytearray  = new byte [(int)myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray,0,mybytearray.length);
                        os = server.getOutputStream();
                        System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + "bytes)");    
                        os.write(mybytearray,0,mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
 

//                        JFrame frame = new JFrame();
//                        frame.getContentPane().add(new JLabel(new ImageIcon(bimg)));
//                        frame.pack();
//                        frame.setVisible(true);
                        dout.flush();
                        System.out.println("Video sent!!!!"); 
                        

                    }
                
                catch(IOException e)
                {
                    System.out.println("Image NOT SENT :(");        
                }
              }
           } 
            catch(SocketTimeoutException st) 
            { 
                System.out.println("Socket timed out!"); 
                break; 
            } 
            catch(IOException e) 
            { 
                e.printStackTrace(); 
                break; 
            } 
            catch(Exception ex) 
            { 
                System.out.println(ex); 
            } 
            finally
            {
           
                try 
                {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    din.close();
                    dout.close();
                    server.close();
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(ImageServer.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
        }
    } 
    public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, Exception 
    {
        byte b[]=new byte[1024];
        FileInputStream f=new FileInputStream("C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Videos/videos.txt");
        DatagramSocket dsoc=new DatagramSocket(9999);
        int i=0;
        while(f.available()!=0)
        {
                    b[i]=(byte)f.read();
                    i++;
        }                     
        f.close();
        dsoc.send(new DatagramPacket(b,i,InetAddress.getLocalHost(),1000)); //DaddyServer PORT: 1000.
        System.out.println("Video-Info File Sent.");

    }
    
    
    
}
