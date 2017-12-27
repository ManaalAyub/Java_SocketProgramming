package socketprogramming.ServerSide;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.*; 
import java.io.*; 
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ImageServer extends Thread 
{ 
    static ServerSocket serverSocket; 
    Socket server; 
    public static BufferedImage bimg; 
    DataInputStream din;
    DataOutputStream dout;

    public ImageServer(Socket clientSocket) throws IOException, SQLException, ClassNotFoundException, Exception 
    { 
        System.out.println("I am IMAGE Server.");
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
                      System.out.println("Sending Image.");
                    try {

                        //image read as buffered image. encoded to string. String decoded to byte array. 
                        //This array written to socket output stream.  
                        //Following Code: Saves Image in Bytes and then makes an image from them.
                        int i=1;

                        bimg = ImageIO.read(new File("C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Images/"+i+".jpg")); 
                        ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
                        ImageIO.write(bimg, "jpg", baos);
                        baos.flush();
                        String base64String=Base64.encode(baos.toByteArray());
                        baos.close();
                        byte[] bytearray = Base64.decode(base64String);
                        //Reading From Byte Array:
                        BufferedImage imageFromBytes=ImageIO.read(new ByteArrayInputStream(bytearray));
                        //Writing Image to Socket Outstream:
                        ImageIO.write(imageFromBytes,"JPG",dout); 

//                        JFrame frame = new JFrame();
//                        frame.getContentPane().add(new JLabel(new ImageIcon(bimg)));
//                        frame.pack();
//                        frame.setVisible(true);
                        dout.flush();
                        System.out.println("Image sent!!!!"); 
                        

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
        FileInputStream f=new FileInputStream("C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Images/images.txt");
        DatagramSocket dsoc=new DatagramSocket(7777);
        int i=0;
        while(f.available()!=0)
        {
                    b[i]=(byte)f.read();
                    i++;
        }                     
        f.close();
        dsoc.send(new DatagramPacket(b,i,InetAddress.getLocalHost(),1000)); //DaddyServer PORT: 1000.
        System.out.println("Image-Info File Sent.");

    }
 
}
