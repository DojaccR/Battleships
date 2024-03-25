/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dorian Jacobs
 */
public class CPU {
    private String[][] playerFirePattern = new String[100][2];
    private String[][] playerPlacementPattern = new String[100][2];
    private String[][] playerOrientationPattern = new String[2][2];
    
    
    private ArrayList<String> cpuFired = new ArrayList<>();
    private int pickedFireCoords = 0;//increments placement pattern array to find next best coords
    private String recentHit = "";//stores most recent hit
    private String beforeRecentHit = "";//stores 1 hit before most recent hit
    private int dir = 0;//0,1 are positive negative increment in direction of most likely orientation, 2,3 are positive negative increment in less likely orientation
    private boolean prevShotHit = false;
    private boolean onMission = false;//whether cpu needs to search surrounding of previously hit node
    private String[] consecutiveNodesHit = new String[5];//records how many nodes of same ship is hit to estimate if a ship is destroyed
    private int consecutiveNodesHitCount = 0;
    
    public CPU(){
        try {
            Scanner sc = new Scanner(new File("gridTendancy.txt"));
            sc.next();
            
            int row = 0;
            while(sc.hasNext()){
                
                Scanner token = new Scanner(sc.next()).useDelimiter("#");
                playerPlacementPattern[row][0] = token.next();
                playerPlacementPattern[row][1] = token.next();
                
                token.close();
                //System.out.println(playerPlacementPattern[row][0] + " " + playerPlacementPattern[row][1]);
                row++;
                
            }
            
            sc.close();
            
            sc = new Scanner(new File("firingTendancy.txt"));
            sc.next();
            
            row = 0;
            while(sc.hasNext()){
                
                Scanner token = new Scanner(sc.next()).useDelimiter("#");
                playerFirePattern[row][0] = token.next();
                playerFirePattern[row][1] = token.next();
                token.close();
                row++;
                
            }
            sc.close();
            
            sc = new Scanner(new File("orientationTendancy.txt"));
            sc.next();
            
            row = 0;
            while(sc.hasNext()){
                
                Scanner token = new Scanner(sc.next()).useDelimiter("#");
                playerOrientationPattern[row][0] = token.next();
                playerOrientationPattern[row][1] = token.next();
                
                token.close();
                row++;
                
            }
            sc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        sortArray(playerPlacementPattern);
        sortArray(playerFirePattern);
        sortArray(playerOrientationPattern);
    }
    
    public void setConsecutiveNodesHit(String s){
        for (int i = 0; i < consecutiveNodesHitCount; i++) {
            System.out.println(consecutiveNodesHit[i]);
        }
        System.out.println(recentHit);
        if(s.equals("empty")){
            for (int i = 0; i < 5; i++) {
                consecutiveNodesHit[i] = null; 
            }
            consecutiveNodesHit[0] = s;
            consecutiveNodesHitCount = 1;
        }
        else if(consecutiveNodesHitCount==0||
           s.substring(0,1).equals(String.valueOf(Integer.parseInt(consecutiveNodesHit[consecutiveNodesHitCount-1].substring(0,1)) + 1))||
           s.substring(0,1).equals(String.valueOf(Integer.parseInt(consecutiveNodesHit[consecutiveNodesHitCount-1].substring(0,1)) - 1))||
           s.substring(2,3).equals(String.valueOf(Integer.parseInt(consecutiveNodesHit[consecutiveNodesHitCount-1].substring(2,3)) + 1))||
           s.substring(2,3).equals(String.valueOf(Integer.parseInt(consecutiveNodesHit[consecutiveNodesHitCount-1].substring(2,3)) - 1))
           )
        {
            consecutiveNodesHit[consecutiveNodesHitCount] = s;
            consecutiveNodesHitCount++;
        }
        else
        {
            for (int i = 0; i < 5; i++) {
                consecutiveNodesHit[i] = null; 
            }
            consecutiveNodesHit[0] = s;
            consecutiveNodesHitCount = 1;
        }
        
        
    }
    
    public ArrayList getCPUFired(){
        return cpuFired;
    }
    
    public void addToCPUFired(String s){
        cpuFired.add(s);
    }
    
    public void setRecentHit(String s){
        beforeRecentHit = recentHit;
        recentHit = s;
    }
    
    public void toggleOnMission(){
        onMission = true;
    }
    
    public boolean getOnMission(){
        return onMission;
    }
    public void incrementDir(){
        if(dir==3){
            dir = 0;
            onMission = false;
        }
        else{
            dir++;
        }
        System.out.println(dir);
    }
    
    public void setPrevShotHit(Boolean b){
        prevShotHit = b;
    }
    
    
    
    public void sortArray(String[][] s){
        //basic sort
        for (int i = 0; i < s.length-2; i++) {
            //System.out.println(i);
            //System.out.println(s[i][1]);
            if(Integer.parseInt(s[i][1]) < Integer.parseInt(s[i+1][1])){
                String temp[][] = {s[i]};
                s[i] = s[i+1];
                s[i+1] = temp[0];
            }
        }
    }
    
    /*
    -cpu firing can be based off of search algorithms
    -personalised pattern lists in conjunction with whole player pool list weighted towards personalised
    -at the end of the day if player knows themself and the cpu algorithm very unlikely cpu will have more than 50% winrate
    */
    public String fireCoords(){
        String fire = "";
        if(onMission == true){
            while(fire.equals("")){
                if(playerOrientationPattern[0][0].equals("h")){
                    if(dir == 0&&(int)(recentHit.charAt(0))-47<10){
                    fire = String.valueOf((int)(recentHit.charAt(0))-47) + "," + recentHit.charAt(2);
                    }else if(dir == 0&&(int)(recentHit.charAt(0))-47>10){
                    dir++;
                    }
                
                    System.out.println("if 1" + dir);
                
                    if((dir == 1&&(int)(recentHit.charAt(0))-49>0)){
                    fire = String.valueOf((int)(recentHit.charAt(0))-49) + "," + recentHit.charAt(2);
                    }else if((dir == 1&&(int)(recentHit.charAt(0))-49<0)){
                    dir++;
                    }
                
                    System.out.println("if 2" + dir);
                
                    if((dir == 2&&(int)(recentHit.charAt(2))-47<10)){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-47);
                    }else if((dir == 2&&(int)(recentHit.charAt(2))-47>10)){
                    dir++;
                    }
                
                    System.out.println("if 3" + dir);
                
                    if(dir == 3&&(int)(recentHit.charAt(2))-49>0){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-49);
                    }else if(dir == 3&&(int)(recentHit.charAt(2))-49<0){
                    dir++;
                    }
                
                    else{
                    System.out.println("Recent:" + recentHit + " dir:" + dir + " next:" + ((int)(recentHit.charAt(0))+1));
                    }
                }
                else if(playerOrientationPattern[0][0].equals("v")){
                    if(dir == 2&&(int)(recentHit.charAt(0))-47<10){
                    fire = String.valueOf((int)(recentHit.charAt(0))-47) + "," + recentHit.charAt(2);
                    }else if(dir == 2&&(int)(recentHit.charAt(0))-47>10){
                    dir++;
                    }
                
                    if(dir == 3&&(int)(recentHit.charAt(0))-49>0){
                    fire = String.valueOf((int)(recentHit.charAt(0))-49) + "," + recentHit.charAt(2);
                    }else if(dir == 3&&(int)(recentHit.charAt(0))-49<0){
                    dir++;
                    }
                
                    if(dir == 0&&(int)(recentHit.charAt(2))-47<10){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-47);
                    }else if(dir == 0&&(int)(recentHit.charAt(2))-47>10){
                    dir++;
                    }
                
                    if(dir == 1&&(int)(recentHit.charAt(2))-49>0){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-49);
                    }else if(dir == 1&&(int)(recentHit.charAt(2))-49<-1){
                    dir++;
                    }
                
                    else{
                    System.out.println("Recent:" + recentHit + " dir:" + dir + " next" + (int)(recentHit.charAt(0))+1);
                    }
                }else{
                System.out.println(playerOrientationPattern[0][0]);
                }
            }
            /*if(playerOrientationPattern[0][0].equals("h")){
                if(dir == 0&&(int)(recentHit.charAt(0))-47<10){
                    fire = String.valueOf((int)(recentHit.charAt(0))-47) + "," + recentHit.charAt(2);
                }else if((dir == 1&&(int)(recentHit.charAt(0))-49>-1)){
                    fire = String.valueOf((int)(recentHit.charAt(0))-49) + "," + recentHit.charAt(2);
                }else if((dir == 2&&(int)(recentHit.charAt(2))-47<10)){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-47);
                }else if(dir == 3&&(int)(recentHit.charAt(2))-49>-1){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-49);
                }else{
                    System.out.println("Recent:" + recentHit + " dir:" + dir + " next:" + ((int)(recentHit.charAt(0))+1));
                }
            }
            else if(playerOrientationPattern[0][0].equals("v")){
                if(dir == 2&&(int)(recentHit.charAt(0))-47<10){
                    fire = String.valueOf((int)(recentHit.charAt(0))-47) + "," + recentHit.charAt(2);
                }else if(dir == 3&&(int)(recentHit.charAt(0))-49>-1){
                    fire = String.valueOf((int)(recentHit.charAt(0))-49) + "," + recentHit.charAt(2);
                }else if(dir == 0&&(int)(recentHit.charAt(2))-47<10){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-47);
                }else if(dir == 1&&(int)(recentHit.charAt(2))-49>-1){
                    fire = recentHit.charAt(0) + "," + String.valueOf((int)(recentHit.charAt(2))-49);
                }else{
                    System.out.println("Recent:" + recentHit + " dir:" + dir + " next" + String.valueOf(((int)(recentHit.charAt(0)))-47));
                }
            }else{
                System.out.println(playerOrientationPattern[0][0]);
            }
            
            if(consecutiveNodesHitCount==4){
                onMission = false;
            }
            System.out.println("from mission"); */   
                 
        }
        else{
            while(fire.equals("")){
                if(cpuFired.contains(playerPlacementPattern[pickedFireCoords][0])){
                    pickedFireCoords++;
                } else {
                    System.out.println("from list");
                    fire = playerPlacementPattern[pickedFireCoords][0];
                    pickedFireCoords++;
                }
                
                
            }
            
           
        }
        return fire;
    }
    
    /*sum the danger level of all the nodes of the boat and find line with the least sum of potential danger to place ship*/
    public String placementCoords(Ship s){
        
        return "";
    }
        
}
