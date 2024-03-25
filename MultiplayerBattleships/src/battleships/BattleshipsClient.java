package battleships;

// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class BattleshipsClient  {
    Socket s1 = null;
    String line = "";
    BufferedReader br = null;
    BufferedReader is = null;
    PrintWriter os = null;
    InetAddress address = null;
    GameScreen d = null;
    SignUp su = null;
    String clientName;


    public BattleshipsClient(){
        try {
            address=InetAddress.getByName(JOptionPane.showInputDialog("Enter IP (for main server)"));
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    
    
        try {
            s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        su = new SignUp(this);
        LogIn();
    }

    public void LogIn(){
        
        String input[] = {};
    
        input = su.waitForEnter().split("#");

    
        String[] response;
        try{
            //br.readLine(); 
            //////////////////////////////////////////////////////////////////////////////////////////////
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

    
    public void GameScreenComs(GameScreen d){

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            String[] response;
            System.out.println("say hi to server");
            d.updateInstruction("Say hi to the server!");
            try{
            while(line.compareTo("QUIT")!=0){
                System.out.println("preread");
                String out = is.readLine();
                System.out.println("postread");
                System.out.println("from server :" + out);
                response = out.split("#");
            
                if(response[0].equals("cout")){
                    d.updateConsole(response[1]);
                }else if(response[0].equals("disp")){
                    d.updateDisplay(response[1]);
                }else if(response[0].equals("ins")){
                    d.updateInstruction(response[1]);
                }else if(response[0].equals("sout")){
                    System.out.println(response[1]);
                }else if(response[0].equals("hcout")){
                    d.setPlayerList(response[1]);
                }else if(response[0].equals("userIn")){
                    boolean nullAns = true;
                    while(nullAns){
                        line=d.waitForEnter();
                        if(line!=null){
                            nullAns = false;
                        }
                    }
                    os.println(/*"reply#" + */line);
                    os.flush();
                    d.clearTextIn();
                
                }else if(response[0].equals("placeShip")){
                    System.out.println("placing ship icon");
                    d.placeShip(Integer.parseInt(response[1]), Integer.parseInt(response[2]), Integer.parseInt(response[3]), Integer.parseInt(response[4]), Integer.parseInt(response[5]));

                }else if(response[0].equals("shipNum")){
                    
                    d.setShipNum(Integer.parseInt(response[1]));
                }else if(response[0].equals("nodeNum")){
                    
                    d.setNodeNum(Integer.parseInt(response[1]));
                }else if(response[0].equals("hitNode")){
                    
                    d.destroyNode(Integer.parseInt(response[1]), Integer.parseInt(response[2]));
                }
            }
                //System.out.println("Server Response : "+response[1]);
                //d.updateInstruction("Server Response : "+response[1]);
                //line=br.readLine();
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
        }
            

        

    }
