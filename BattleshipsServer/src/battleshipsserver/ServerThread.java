package battleshipsserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerThread extends Thread{  
    
    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;
    String clientKey;
    String clientName;
    String Username;
    String Password;
    String clientGameType;
    private boolean ready = false;
    private GameControl gc;
    
    
    public ServerThread(Socket s, String key, GameControl gc){
        this.s = s;
        this.clientKey = key;
        this.gc = gc;
        
        

    }

        
        //main page
/* 
        boolean gameStart = false;
        while(!gameStart){
            try{
                is= new BufferedReader(new InputStreamReader(s.getInputStream()));
                os=new PrintWriter(s.getOutputStream());
        
            }catch(IOException e){
                System.out.println("IO error in server thread");
            }
        
            try {
                line=is.readLine();
                while(line.compareTo("START")!=0){
        
                    os.println("sout#" + line);
                    os.flush();
                    System.out.println("Response to Client" + clientKey + "  :  "+line);
                    line=is.readLine();
                }   
            } catch (IOException e) {
        
                line=this.getName(); //reused String line for getting thread name
                System.out.println("IO Error/ Client "+line+" terminated abruptly");
            }
            catch(NullPointerException e){
                line=this.getName(); //reused String line for getting thread name
                System.out.println("Client "+line+" Closed");
            }
            

        }
        */
        //clientGameType = fetchFromClient();
        

    
    
    public void signUp(){
        //figure out registration vs login
        //implement login
        
        boolean verifiedUser = false;
        while(!verifiedUser){
            String out = fetchFromClient();
            System.out.println(out);
            String[] response = out.split("#");
            
            if(response[0].equals("register")){
                try {
                    Scanner sc = new Scanner(new File("RegisteredUsers.txt"));
                    String users = "";
                    while(sc.hasNext()){
                        users += sc.next() + "\n";
                    }
                    sc.close();
                    users += response[1] + "#" + response[2];
                    FileWriter fw = new FileWriter("RegisteredUsers.txt");
                    fw.write(users);
                    fw.close();
                    verifiedUser = true;
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                clientName = response[1];
            }else{
                try {
                    Scanner sc = new Scanner(new File("RegisteredUsers.txt"));
                    HashMap<String, String> users = new HashMap<>();
                    while(sc.hasNext()){
                        Scanner token = new Scanner(sc.next()).useDelimiter("#");
                        users.put(token.next(), token.next());
                    }
                    sc.close();
                    if(users.containsKey(response[1])&&users.get(response[1]).equals(response[2])){
                        verifiedUser = true;
                        
                    }else{
                        
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                clientName = response[1];
            }
            if(verifiedUser == true){
                ready = true;
                sendToClient("verified");
            }else{
                sendToClient("unverified");
            }
        }

    }

    public boolean getReady(){
        return ready;
    }

    
    public String getKey(){
        return clientKey;
    }
    
    public String getClientName(){
        return clientName;
    }
    
    public void sendToClient(String str){
        try {
            os=new PrintWriter(s.getOutputStream());
            os.println(str);
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String fetchFromClient(){
        try {
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            return is.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void sendToServer(){
        
    }
    
    public void fetchFromServer(){
        
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 
    public void run() {
    try{
        is= new BufferedReader(new InputStreamReader(s.getInputStream()));
        os=new PrintWriter(s.getOutputStream());

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }

    try {
        line=is.readLine();
        while(line.compareTo("QUIT")!=0){

            os.println("sout#" + line);
            os.flush();
            System.out.println("Response to Client" + clientKey + "  :  "+line);
            line=is.readLine();
        }   
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client "+line+" terminated abruptly");
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client "+line+" Closed");
    }

    finally{    
    try{
        
        System.out.println("Connection Closing..");
        
        if (is!=null){
            is.close(); 
            System.out.println(" Socket Input Stream Closed");
        }

        if(os!=null){
            os.close();
            System.out.println("Socket Out Closed");
        }
        if (s!=null){
        s.close();
        System.out.println("Socket Closed");
        }

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    }//end finally
    }
    */

}
