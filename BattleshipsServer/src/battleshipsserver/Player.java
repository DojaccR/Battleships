package battleshipsserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Player {
    private Ship[] playerShips;
    private int shipPlaced = 0;
    private ArrayList<String> playerFired = new ArrayList<>();
    private ArrayList<String> playerPlaced = new ArrayList<>();
    private HashMap<String, Integer> playerFirePattern = new HashMap<>();
    private HashMap<String, Integer> playerPlacementPattern = new HashMap<>();

    public Ship[] getPlayerShips() {
        return playerShips;
    }

    public int getShipPlaced() {
        return shipPlaced;
    }

    public ArrayList<String> getPlayerFired() {
        return playerFired;
    }
    
    public void addPlayerFired(String s){
        playerFired.add(s);
    }

    public ArrayList<String> getPlayerPlaced() {
        return playerPlaced;
    }

    public HashMap<String, Integer> getPlayerFirePattern() {
        return playerFirePattern;
    }

    public HashMap<String, Integer> getPlayerPlacementPattern() {
        return playerPlacementPattern;
    }

    public void setPlayerShips(Ship[] playerShips) {
        this.playerShips = playerShips;
    }

    public void placeShip(ServerThread p, int[] gridSize){
        for (int i = 0; i < playerShips.length; i++) {
            boolean valid = false;
            while(valid == false){
                boolean uiVal = false;
                String[] sCor = {"", "", ""};
                while(uiVal == false){
                    
                    /*sCor = JOptionPane.showInputDialog("Ship is " + playerShips[i].getLength() + " long.\nEnter Ship co-ordinate and orientation (h = horizontal, v = vertical)in form 'x,y,o'"
                        + "\nNote the co-ordinate entered will be the starting point of ship extending in positive direction of orientation.").split(",");*/
                    
                    p.sendToClient("ins#<html>Ship is " + playerShips[i].getLength() + " long.<br>Enter Ship co-ordinate and orientation (h = horizontal, v = vertical)in form 'x,y,o'"
                        + "<br>Note the co-ordinate entered will be the starting point of ship extending in positive direction of orientation.<htmp>");
                    /*while(!d.getHConsole().equals("pressed")){
                    d.getButton().addActionListener(new ActionListener() {
                    @Override
                     public void actionPerformed(ActionEvent e) {
                            String[] sCor = d.getTxtIn().split(",");
                            d.setHConsole("pressed");          
                        }
                    });
                    }
                    */
                    p.sendToClient("userIn");
                    sCor = p.fetchFromClient().split(",");
                    
                    //d.setHConsole("");
                    
                    
                    //String[] validCoArr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                    ArrayList<String> validCo = new ArrayList<>(Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
                    if(sCor.length==3&&validCo.contains(sCor[0])&&validCo.contains(sCor[1])&&(sCor[2].equals("v")||sCor[2].equals("h"))){
                        uiVal = true;
                    }else if(sCor.length==2&&validCo.contains(sCor[0])&&validCo.contains(sCor[1])&&playerShips[i].getLength()==1){
                        uiVal = true;
                    }else{
                        System.out.println("Invalid format or values exceed grid");
                        p.sendToClient("cout#Invalid format or values exceed grid");
                    }
                }
                if(playerShips[i].getLength()!=1){
                    playerShips[i].setShipOrientation(sCor[2]);
                }else{
                    playerShips[i].setShipOrientation("h");
                }
                playerShips[i].setNodes(sCor[0] + "," + sCor[1]);
                if(playerShips[i].getShipOrientation().equals("v")){
                    int check = 0;
                    if((Integer.parseInt(sCor[1])+ this.playerShips[i].getLength()-1) < gridSize[1]){
                        check++;
                        System.out.println(check + "grid");
                    }
                    boolean validNodes = true;
                    for (int j = 0; j < i; j++) {
                        System.out.println(j);
                        for (int k = 0; k < playerShips[j].getLength(); k++) {
                            for (int l = 0; l < playerShips[i].getLength(); l++) {
                                if(playerShips[j].getNode(k).equals(playerShips[i].getNode(l))){
                                    validNodes = false;
                                }
                            }
                        }
                    }
                    
                    if(validNodes == true ){
                        check++;
                        System.out.println(check + "node");
                    }
                    if(check == 2){
                        valid = true;
                        
                        for (int j = 0; j < playerShips[i].getLength(); j++) {
                            playerPlacementPattern.put(playerShips[i].getNode(j), 1);
                        }
                        
                        shipPlaced++;
                        //orientationTally[1]++;
                        p.sendToClient("disp#" + this.boardToText());
                        p.sendToClient("placeShip#" + playerShips[i].getLength() + "#1#" + sCor[0] + "#" + sCor[1] + "#" + i);
                        System.out.println(this.boardToText());
                    }else{
                        p.sendToClient("cout#Invalid placement");
                        System.out.println("Invalid placement!" + String.valueOf(check));
                    }
                }else{
                    int check = 0;
                    if((Integer.parseInt(sCor[0])+ this.playerShips[i].getLength()-1) < gridSize[0]){
                        check++;
                        System.out.println("valid grid");
                    }
                    boolean validNodes = true;
                    for (int j = 0; j < i; j++) {
                        for (int k = 0; k < playerShips[j].getLength(); k++) {
                            for (int l = 0; l < playerShips[i].getLength(); l++) {
                                if(playerShips[j].getNode(k).equals(playerShips[i].getNode(l))){
                                    validNodes = false;
                                }
                            }
                        }
                    }
                    
                    if(validNodes == true){
                        check++;
                        System.out.println("valid node");
                    }
                    if(check == 2){
                        valid = true;
                        
                        for (int j = 0; j < playerShips[i].getLength(); j++) {
                            playerPlacementPattern.put(playerShips[i].getNode(j), 1);
                        }
                        
                        shipPlaced++;
                        p.sendToClient("disp#" + this.boardToText());
                        p.sendToClient("placeShip#" + playerShips[i].getLength() + "#0#" + sCor[0] + "#" + sCor[1] + "#" + i);
                        System.out.println(this.boardToText());
                        //orientationTally[0]++;
                    }else{
                        p.sendToClient("cout#Invalid placement");
                        System.out.println("Invalid placement!" + String.valueOf(check));
                    }                
                }
            }
            
        }
        
        
    }
    
    
    
    public String boardToText(){
        if(System.getProperty("os.name").contains("OS")){
        String d = "<html>&nbsp;&nbsp;-";
        for (int n = 0; n < 10; n++) {
                d += "-" + String.valueOf(n) + "--";
            }
        for (int m = 0; m < 10; m++) {
            
            
            d+="<br>" + m;
            //boolean validNodes = true;
            
            for (int i = 0; i < 10; i++) {
                d+="|";
                boolean placed = false;
                for (int j = 0; j < shipPlaced; j++) {
                    
                    for (int k = 0; k < playerShips[j].getLength(); k++) {
                        
                        if(playerShips[j].getNode(k).equals(String.valueOf(i) + "," + String.valueOf(m))){
                            d+="&nbsp;&nbsp;&nbsp;o&nbsp;&nbsp;";
                            placed = true;
                        }else if(playerShips[j].getNode(k).contains("d")&&playerShips[j].getNode(k).contains(String.valueOf(i) + "," + String.valueOf(m))){
                            d+="&nbsp;&nbsp;&nbsp;x&nbsp;&nbsp;";
                            placed = true;
                        }
                    }
                    
                }
                if(placed==false){
                        d+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
            }
            d+="|<br>&nbsp;&nbsp;";
            
           for (int n = 0; n < 41; n++) {
                d += "-";
            } 
        }
        
        d+="<html>";
        return d;        
        }
        ////////////////////////////////////////////////////////////////////////////
        else{
                String d = "<html>&nbsp;&nbsp;";
        for (int n = 0; n < 10; n++) {
                d += "---" + String.valueOf(n) + "---";
            }
        for (int m = 0; m < 10; m++) {
            
            
            d+="<br>" + m;
            //boolean validNodes = true;
            
            for (int i = 0; i < 10; i++) {
                d+="|";
                boolean placed = false;
                for (int j = 0; j < shipPlaced; j++) {
                    
                    for (int k = 0; k < playerShips[j].getLength(); k++) {
                        
                        if(playerShips[j].getNode(k).equals(String.valueOf(i) + "," + String.valueOf(m))){
                            d+="&nbsp;&nbsp;&nbsp;o&nbsp;&nbsp;&nbsp;&nbsp;";
                            placed = true;
                        }else if(playerShips[j].getNode(k).contains("d")&&playerShips[j].getNode(k).contains(String.valueOf(i) + "," + String.valueOf(m))){
                            d+="&nbsp;&nbsp;&nbsp;x&nbsp;&nbsp;&nbsp;&nbsp;";
                            placed = true;
                        }
                    }
                    
                }
                if(placed==false){
                        d+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
            }
            d+="|<br>&nbsp;&nbsp;&nbsp;&nbsp;";
            
           for (int n = 0; n < 75; n++) {
                d += "-";
            } 
        }
        
        d+="<html>";
        return d; 
        }
    }
}
