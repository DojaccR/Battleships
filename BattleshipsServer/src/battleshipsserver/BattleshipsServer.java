package battleshipsserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BattleshipsServer {
    
    private static int clientCount = 0;
    public static void main(String args[]){

        GameControl gc = new GameControl();
        ServerMain sm = new ServerMain();
        FileWriter fw=null;
        try{
            Socket s=null;
            ServerSocket ss2=null;
            System.out.println("Server Listening......");
            try{
                ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined

            }
            catch(IOException e){
                e.printStackTrace();
                System.out.println("Server error");
                
            }
            
            Scanner sc = new Scanner(new File("PlayersConnected.txt"));
            fw = new FileWriter(new File("PlayersConnected.txt"));
            while(clientCount<2){
                try{
                    s= ss2.accept();
                    
                    System.out.println("connection Established");
                    
                    ServerThread st=new ServerThread(s, String.valueOf(clientCount), gc);
                    
                    st.start();
                    
                    gc.addPlayer(st);
                    //sm.addClient(st);
                    
                    //System.out.println(x);
                    
                    clientCount++;
                }
                
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Connection Error");
                    
                }
            }
            
            System.out.println("running game");
            gc.run();

        }
        catch(IOException ex){
            Logger.getLogger(BattleshipsServer.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(BattleshipsServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

}
}

