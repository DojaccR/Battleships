package battleships;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class BattleshipsClient  {
    private Socket s1 = null;
    private String line = "";
    private BufferedReader br = null; // User input 
    private BufferedReader is = null; // Server message
    private PrintWriter os = null; // Message to send to server
    private InetAddress address = null;
    private GameScreen gameScreen = null;
    private SignUp su = null;
    private String clientName;


    public BattleshipsClient(){
        try{
	    // Getting InetAddress from user
            address=InetAddress.getByName(JOptionPane.showInputDialog("Enter IP (for main server)"));
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    
    
        try {
	    // Connecting to address on port 4445, can change if port not available, server
	    // will need to change port as well.
            s1=new Socket(address, 4445);

	    // Assigning br to take user input 
            br= new BufferedReader(new InputStreamReader(System.in));

	    // Assigning is to be input stream from server 
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));

	    // Assigning os to be output stream to server
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            // Probably not the best to lump all of these together but is what
	    // it is
	    e.printStackTrace();
            System.err.print("IO Exception");
        }

	// Echoing some stuff
        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

	// Begining user login stage
        su = new SignUp(this);
        LogIn();
    }

    public void LogIn(){
        
        String input[] = {};
    
        input = su.waitForEnter().split("#");

    
        String[] response;
        try{
            boolean verifiedUser = false;
            while(verifiedUser == false){

                os.println(input[0] + "#" + input[1] + "#" + input[2]);
                os.flush();
                su.clearTextIn();
            
                String out = is.readLine();
                System.out.println(out);
                response = out.split("#");
            
                if(response[0].equals("verified")){
                    verifiedUser = true;
                }else{
                    input = su.waitForEnter().split("#");
                }
            }
            clientName = input[0];
            su.toMain();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Socket read Error");
            }
            

    }

    public void mainsScreenComs(){


    }

    
    public void GameScreenComs(GameScreen gameScreen){
            String[] response;
            System.out.println("say hi to server");
            gameScreen.updateInstruction("Say hi to the server!");
            try{
            while(line.compareTo("QUIT")!=0){
                System.out.println("preread");
                String out = is.readLine();
                System.out.println("postread");
                System.out.println("from server :" + out);
                response = out.split("#");
            
                if(response[0].equals("cout")){
                    gameScreen.updateConsole(response[1]);
                }else if(response[0].equals("disp")){
                    gameScreen.updateDisplay(response[1]);
                }else if(response[0].equals("ins")){
                    gameScreen.updateInstruction(response[1]);
                }else if(response[0].equals("sout")){
                    System.out.println(response[1]);
                }else if(response[0].equals("hcout")){
                    gameScreen.setPlayerList(response[1]);
                }else if(response[0].equals("userIn")){
                    boolean nullAns = true;
                    while(nullAns){
                        line=gameScreen.waitForEnter();
                        if(line!=null){
                            nullAns = false;
                        }
                    }
                    os.println(/*"reply#" + */line);
                    os.flush();
                    gameScreen.clearTextIn();
                
                }else if(response[0].equals("placeShip")){
                    System.out.println("placing ship icon");
                    gameScreen.placeShip(Integer.parseInt(response[1]), Integer.parseInt(response[2]), Integer.parseInt(response[3]), Integer.parseInt(response[4]), Integer.parseInt(response[5]));

                }else if(response[0].equals("shipNum")){
                    
                    gameScreen.setShipNum(Integer.parseInt(response[1]));
                }else if(response[0].equals("nodeNum")){
                    
                    gameScreen.setNodeNum(Integer.parseInt(response[1]));
                }else if(response[0].equals("hitNode")){
                    
                    gameScreen.destroyNode(Integer.parseInt(response[1]), Integer.parseInt(response[2]));
                }
            }
            }catch(Exception e){
                System.out.println("readline error");
            }
                
        }
        
        public void EndScreen(){


        }
        
        public void close(){
                try {
                    is.close();
                    os.close();
                    br.close();
                    s1.close();
                    System.out.println("Connection Closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
        }
            

        

    }
