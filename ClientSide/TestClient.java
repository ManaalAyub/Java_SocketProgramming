package socketprogramming.ClientSide;
import java.net.*; 
import java.io.*; 
import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*; 
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import static jdk.nashorn.tools.ShellFunctions.input;


public class TestClient 
{ 
    static Socket client;
    static DataInputStream in;
    static DataOutputStream out;
    // Moved these variables to class-level    
   static private JFrame mainFrame;
   static private JLabel headerLabel;
   static private JLabel statusLabel;
   static private JPanel controlPanel;
   static String type;
   static String subject;
   public String sentence="mathematics"+" & written";
   public final static int FILE_SIZE = 23717000; 
   public final static String VIDEO_TO_RECEIVE ="C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Videos/source-recvd.avi";
   public final static String AUDIO_TO_RECEIVE ="C:/Users/manal/Desktop/Fall2016/CN-Lab/SocketProgramming/src/socketprogramming/ServerSide/Audios/source-recvd.wav";


    /**
     *
     * @param t
     */
    public TestClient()
   {
     prepareGUI();   
   }

   public static void prepareGUI(){
      mainFrame = new JFrame("Online Test System");
      mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   public static String getSubject(){
       return subject;
   }
   public static String getType(){
       return type;
   }
   public void setSubject(String s){
       subject=s;
   }
   public void setType(String t){
       type=t;
   }
   public void showTextFieldDemo(){
      //headerLabel.setText("Control in action: JTextField"); 

      JLabel  namelabel= new JLabel("Subject: ", JLabel.RIGHT);
      JLabel  passwordLabel = new JLabel("Type: ", JLabel.CENTER);
      final JTextField userText = new JTextField(6);
      final JTextField passwordText = new JTextField(6);     
      JButton loginButton = new JButton("Submit");

      
      controlPanel.add(namelabel);
      controlPanel.add(userText);
      controlPanel.add(passwordLabel);       
      controlPanel.add(passwordText);
      controlPanel.add(loginButton);
      mainFrame.setVisible(true); 

      loginButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {     
            subject=userText.getText();
            type=passwordText.getText();
            sentence=subject+" & "+type;
            
            String data = "Subject: " + userText.getText();
            data += " Type: " 
            + new String(passwordText.getText()); 
            statusLabel.setText(data);  
             try {
                 afterSubmitClick(sentence);
             } catch (UnknownHostException ex) {
                 Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
             } catch (Exception ex) {
                 Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
             }

         }
      });  

    }
    public void afterSubmitClick(String input) throws SocketException, UnknownHostException, IOException, Exception{
        
        int choice=2;// -----------------------------> Change this to change RECEIVE category.
        
        input=input+":"+choice;
        //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[100];
        byte[] receiveData = new byte[100];
       // String sentence = test.inFromUser.readLine(); //CHECK THIS!!!
        //String sentence = getSubject()+getType();
        if(!input.isEmpty())
           sendData=input.getBytes();
        else 
            System.out.println("Enter Preference");
        System.out.println("Preference Selected:Category Chosen="+ input);
        //System.out.println("SendData"+sendData.toString());
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String response = new String(receivePacket.getData());
        System.out.println("RECEIVED SUB-SERVER INFO: " + response);
        clientSocket.close();
        String[] subServerInfo = response.split(":");    
        int port = Integer.valueOf(subServerInfo[0]); 
        String serverName=subServerInfo[1];
        //InetAddress addr = InetAddress.getByName(subServerInfo[1]);
        //String serverName = addr.getHostName(); 
        try 
        { 
            System.out.println("Connecting to " + serverName + " on port " + port); 
            client = new Socket(serverName, port); 

            System.out.println("Just connected to " + client.getRemoteSocketAddress()); 
            in=new DataInputStream(client.getInputStream()); 
            out = new DataOutputStream(client.getOutputStream()); 
            
            //1.Reading.
            System.out.println(in.readUTF()); 
            //2.Writing.
            out.writeUTF("Hello from " + client.getLocalSocketAddress());  
            //out.writeUTF("Category Chosen :" + choice);
            
            //Need to add search logic here that matches user preference with tests available.
            if(choice==1)
            {  
                BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(in));
                while (img == null){img = ImageIO.read(in);}
                JFrame frame_Content = new JFrame();
                frame_Content.getContentPane().add(new JLabel(new ImageIcon(img)));
                frame_Content.pack();
                frame_Content.setVisible(true); 
                System.out.println("Image received!!!!"); 
            }
            else if(choice==2)
            {
                
                //if Audio Requested:
//                   int bytesRead;
//                   int current = 0;
//                   FileOutputStream fos = null;
//                   BufferedOutputStream bos = null;
//                  // receive file
//                  byte [] mybytearray  = new byte [FILE_SIZE];
//                  InputStream is = client.getInputStream();
//                  fos = new FileOutputStream(AUDIO_TO_RECEIVE);
//                  bos = new BufferedOutputStream(fos);
//                  bytesRead = is.read(mybytearray,0,mybytearray.length);
//                  current = bytesRead;
//
//                  do {
//                     bytesRead =
//                        is.read(mybytearray, current, (mybytearray.length-current));
//                     if(bytesRead >= 0) current += bytesRead;
//                  } while(bytesRead > -1);
//
//                  bos.write(mybytearray, 0 , current);
//                  bos.flush();
//                  System.out.println("File " + AUDIO_TO_RECEIVE
//                      + " downloaded (" + current + " bytes read)");     

                InputStream bufferedIn= new BufferedInputStream(in);
                play(bufferedIn);
                System.out.println("Audio received!!!!"); 
            }
            else if(choice==3)
            {
                //if Video Requested:
               int bytesRead;
               int current = 0;
               FileOutputStream fos = null;
               BufferedOutputStream bos = null;
              // receive file
              byte [] mybytearray  = new byte [FILE_SIZE];
              InputStream is = client.getInputStream();
              fos = new FileOutputStream(VIDEO_TO_RECEIVE);
              bos = new BufferedOutputStream(fos);
              bytesRead = is.read(mybytearray,0,mybytearray.length);
              current = bytesRead;

              do {
                 bytesRead =
                    is.read(mybytearray, current, (mybytearray.length-current));
                 if(bytesRead >= 0) current += bytesRead;
              } while(bytesRead > -1);

              bos.write(mybytearray, 0 , current);
              bos.flush();
              System.out.println("File " + VIDEO_TO_RECEIVE
                  + " downloaded (" + current + " bytes read)");     
               
               
               System.out.println("Video received!!!!"); 
            }
      
            

        } 
        catch(IOException e) 
        { 
            e.printStackTrace(); 
        } 
        finally{  
            in.close();
            out.close();
            client.close();
        }
   
 
  
    }
    private static synchronized void play(final InputStream in) throws Exception{
        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
        if(ais==null)
            System.out.println("File Format Not Supported.");
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(ais);
            clip.start();
            Thread.sleep(100); // given clip.drain a chance to start
            clip.drain();
        }
    }


    public static void main(String [] args) throws IOException 
    { 
        //UI:
        System.out.println("Enter Preference in TextFields.");
        TestClient ctrl=new TestClient();
        ctrl.showTextFieldDemo();

        
    }
}
