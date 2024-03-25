/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author dorianjacobs
 */
public class GameControl{
    
    /*
    private Ship[] playerShips;
    private Ship[] cpuShips;
    private int shipPlaced = 0;
    private ArrayList<String> playerFired = new ArrayList<>();
    private ArrayList<String> playerPlaced = new ArrayList<>();
    
    private HashMap<String, Integer> playerFirePattern = new HashMap<>();
    private HashMap<String, Integer> playerPlacementPattern = new HashMap<>();
    //need to make hashmap of grid contains node true/false and grid been hit true false
    private int[] orientationTally = {0, 0};
    */
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final int[] gridSize = {10, 10};
    private Thread[] Client = new Thread[8];
    private ServerThread p1 = null;
    private ServerThread p2 = null;
    private Player[] player = new Player[8];
    private Player player1 = new Player();
    private Player player2 = new Player();
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    private String[][] fPA = new String[100][2];//fire pattern array
    private String[][] pPA = new String[100][2];//placement pattern array
    private String[][] pOA = new String[2][2];//placement orientation array
    private int fireOrder = 0;
    */
    public GameControl(){
    }
    
    public void setGrid(String[] g){
        //this.gridSize = {Integer.parseInt(g[0]), Integer.parseInt(g[1])};     
    }
    
    //server side
    /*
    public Ship[] getPlayerShips(){
        return this.playerShips;
    }
    
    //server side
    public Ship[] getCPUShips(){
        return this.cpuShips;
    }
    */
    public void addPlayer(ServerThread t){
        
        //entering players clients/threads into the game
        if(p1 == null){
            p1 = t;
            p1.signUp();
            System.out.println("player 1 connected" + t);
            //p1.sendToClient("hcout#<html>Players1:<br>1. " + p1.getClientName());
        }else{
            p2 = t;
            p2.signUp();
            System.out.println("player 2 connected");
            //p1.sendToClient("hcout#<html>Players:<br>1. " + p1.getClientName() + "<br>2. " + p2.getClientName());
            //p2.sendToClient("hcout#<html>Players:<br>1. " + p1.getClientName() + "<br>2. " + p2.getClientName());
        }
    }
    
    //server side
    public void generateShips(int n){
        
        //creating a standard amount of ships for all players
        
        Ship[] player1Ships = new Ship[n];
        Ship[] player2Ships = new Ship[n];
        Random r = new Random();
        
        //randomly generates a ship length for ship i and assigns this length for all player's ship of index i
        for (int i = 0; i < n; i++) {
            int size = r.nextInt(4)+1;
            player1Ships[i] = new Ship(size);
            player2Ships[i] = new Ship(size);
        }
        
        player1.setPlayerShips(player1Ships);
        player2.setPlayerShips(player2Ships);
        
    }
    
    //server side
    public void fire(String s, ServerThread p, CPU cpu, ServerThread e){
        
        //format s (p)(x)(y)
        // p = player; e = enemy plar
        System.out.println(s);
        
        //splits the input into which player, x coordinate, y coordintate
        String[] fCor = s.split(",");
        
        //preparing to write to file for data on user
        //playerFirePattern.put(fCor[1] + "," + fCor[2], 100-fireOrder);
        //fireOrder++;
        
        
        if(fCor[0].equals("p1")){
            boolean hit = false;
            
            //loops through all nodes that player attacked has to check if fired coordinate is same as a node
            for (int i = 0; i < player2.getPlayerShips().length; i++) {
                for (int j = 0; j < player2.getPlayerShips()[i].getLength(); j++) {
                    if(player2.getPlayerShips()[i].getNode(j).equals(fCor[1] + "," + fCor[2])){
                        player2.getPlayerShips()[i].setNode(j, player2.getPlayerShips()[i].getNode(j) + "d");
                        System.out.println("You scored a hit on " + fCor[1] + "," + fCor[2]);
                        p.sendToClient("cout#You scored a hit on " + fCor[1] + "," + fCor[2]);
                        e.sendToClient("cout#you got hit on " + fCor[1] + "," + fCor[2]);
                        e.sendToClient("hitNode#" + fCor[1] + "#" + fCor[2]);
                        hit = true;
                    }
                }
            }
            if(hit == false){
                e.sendToClient("cout#Player 1 miss");
            }
        }else{
           boolean hit = false;
           for (int i = 0; i < player1.getPlayerShips().length; i++) {
                for (int j = 0; j < player1.getPlayerShips()[i].getLength(); j++) {
                    if(player1.getPlayerShips()[i].getNode(j).equals(fCor[1] + "," + fCor[2])){
                        player1.getPlayerShips()[i].setNode(j, player1.getPlayerShips()[i].getNode(j) + "d");
                        System.out.println("You scored a hit on " + fCor[1] + "," + fCor[2]);
                        p.sendToClient("cout#You scored a hit on " + fCor[1] + "," + fCor[2]);
                        e.sendToClient("cout#you got hit on " + fCor[1] + "," + fCor[2]);
                        e.sendToClient("hitNode#" + fCor[1] + "#" + fCor[2]);
                        hit = true;
                        /*
                        cpu.toggleOnMission();
                        cpu.setRecentHit(fCor[1] + "," + fCor[2]);
                        cpu.setConsecutiveNodesHit(fCor[1] + "," + fCor[2]);
                        cpu.setPrevShotHit(hit);
                        */
                        
                    }
                }
            }
            if(hit == false){
                //cpu.incrementDir();
                e.sendToClient("cout#Player 2 miss");
            }
            /*
            if(hit == false&&cpu.getOnMission()==false){
                cpu.setRecentHit("");
                cpu.setConsecutiveNodesHit("empty");
                
            }
            */
           // need to communicate to cpu if hit or miss
        }
        
    }
    
    //server side
    public boolean isLost(String s){
        boolean lose = true;
        if(s.equals("p1")){
            //check if all ships are destroyed
            for (int i = 0; i < player1.getPlayerShips().length; i++) {
            if(player1.getPlayerShips()[i].isDestroyed() == false){
                lose = false;
            }
            }
        }else if(s.equals("p2")){
            for (int i = 0; i < player2.getPlayerShips().length; i++) {
            if(player2.getPlayerShips()[i].isDestroyed() == false){
                lose = false;
            }
            }
        }

        return lose;
    }
    
    
    public void run(){
        while(!p1.getReady()||!p2.getReady()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }

            
        }
        System.out.println("players in");
        
        System.out.println("running in gc");
        
        //p1.sendToClient("disp#" + player1.boardToText());
        //p2.sendToClient("disp#" + player2.boardToText());
        
        CPU cpu = new CPU();
        
        boolean uiVal = false;
        String noOfShips = "0";
        while(uiVal == false){
            //noOfShips = JOptionPane.showInputDialog("enter no of ships");
            p1.sendToClient("ins#Enter number of ships 0 <= x <= 5");
            p2.sendToClient("cout#Player 1 chosing number of ships");
            p1.sendToClient("userIn");
            
            noOfShips = p1.fetchFromClient();
            
            ArrayList<String> validNum = new ArrayList<>(Arrays.asList(new String[]{"1", "2", "3", "4", "5"}));
            if(validNum.contains(noOfShips)){
                uiVal = true;
            }
        }
        
        p1.sendToClient("shipNum#" + noOfShips);
        p2.sendToClient("shipNum#" + noOfShips);
        
        p1.sendToClient("cout# ");
        p2.sendToClient("cout# ");
        this.generateShips(Integer.parseInt(noOfShips));
        int numNodes = 0;
        for(int i = 0; i < Integer.parseInt(noOfShips); i++){
            numNodes += player1.getPlayerShips()[i].getLength();

        }

        p1.sendToClient("nodeNum#" + numNodes);
        p2.sendToClient("nodeNum#" + numNodes);

        p2.sendToClient("cout#player 1 placing...");
        player1.placeShip(p1, gridSize);
        p2.sendToClient("cout# ");
        p1.sendToClient("cout#player 2 placing...");
        player2.placeShip(p2, gridSize);
        p1.sendToClient("cout# ");
        //Random r = new Random();
        
        while(isLost("p1") == false&&isLost("p2") == false){
            uiVal = false;
            String fCor[] = {"", ""};
            p2.sendToClient("cout#Player 1 Firing");
            while(uiVal == false){
                p1.sendToClient("ins#Enter Co-ordinates to fire'x,y'");
                p1.sendToClient("userIn");
                fCor = p1.fetchFromClient().split(",");
                
                              
                
                    
                    ArrayList<String> validCo = new ArrayList<>(Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
                    if(fCor.length==2&&validCo.contains(fCor[0])&&validCo.contains(fCor[1])&&!player1.getPlayerFired().contains(fCor[0] + "," + fCor[1])){
                        uiVal = true;
                        
                    }else{
                        System.out.println("Invalid format or values exceed grid");
                        p1.sendToClient("cout#Invalid format, values exceed grid or already fired");
                    }
            }
            player1.addPlayerFired(fCor[0] + "," + fCor[1]);
            fire("p1," + fCor[0] + "," + fCor[1], p1, cpu, p2);
            p2.sendToClient("disp#" + player2.boardToText());
            
            if (isLost("p2")==true) {
                
                p1.sendToClient("cout#Congratulations! you've won!");
                p2.sendToClient("cout#Player 1 won! Better luck next time!");
                p1.sendToClient("disp# ");
                p2.sendToClient("disp# ");
                break;
            }
            
            uiVal = false;
            fCor[0] = "";
            fCor[1] = "";
            p1.sendToClient("cout#Player 2 Firing");
            
            while(uiVal == false){
                p2.sendToClient("ins#Enter Co-ordinates to fire'x,y'");
                p2.sendToClient("userIn");
                fCor = p2.fetchFromClient().split(",");
                
                    ArrayList<String> validCo = new ArrayList<>(Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
                    if(fCor.length==2&&validCo.contains(fCor[0])&&validCo.contains(fCor[1])&&!player2.getPlayerFired().contains(fCor[0] + "," + fCor[1])){
                        uiVal = true;
                        //playerFirePattern.put(fCor[0] + "," + fCor[1], fireOrder);
                        //fireOrder++;
                    }else{
                        System.out.println("Invalid format or values exceed grid");
                        p2.sendToClient("cout#Invalid format, values exceed grid or already fired");
                    }
            }
            player2.addPlayerFired(fCor[0] + "," + fCor[1]);
            fire("p2," + fCor[0] + "," + fCor[1], p2, cpu, p1);
            p1.sendToClient("disp#" + player1.boardToText());
            
            if(isLost("p1")){
                
                p2.sendToClient("cout#Congratulations! you've won!");
                p1.sendToClient("cout#Player 2 won! Better luck next time!");
                p1.sendToClient("disp# ");
                p2.sendToClient("disp# ");
                
                break;
            }
            
        }
        
    }
    
   
}

