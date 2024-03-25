/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsserver;

/**
 *
 * @author Dorian Jacobs
 */
public class ServerMain {
    private static ServerThread[] Client = new ServerThread[8];
    private static int clientCount = 0;
    public ServerMain(){
    }
    
    public void run(){
          
    }
    
    public void addClient(ServerThread t){
        Client[clientCount] = t;
        clientCount++;
    }
    
    
    
    
}
