/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsserver;

/**
 *
 * @author dorianjacobs
 */
public class Ship {
    
    private int slength;
    private String sOrientation;
    private String[] node;
    
    public Ship(){
    
    }
    
    public Ship(int l){
        slength = l;
        node = new String[l];
    }
    
    public void setLength(int l){
        slength = l;
        node = new String[l];
    }
    
    public void setShipOrientation(String o){
        sOrientation = o;
    }
    
    public String getShipOrientation(){
        return sOrientation;
    }
    
    public int getLength(){
        return slength;
    }
    
    public String getNode(int x){
        return node[x];
    }
    public void setNode(int x, String s){
        node[x] = s;
    }
    
    public boolean isDestroyed(){
        boolean isDestroyed = true;
        for (int i = 0; i < slength; i++) {
            if(!node[i].contains("d")){
                isDestroyed = false;
            }
        }
        
        return isDestroyed;
    }
    
    public void setNodes(String n){
        String inpCor[] = n.split(",");
        if(sOrientation.equals("h")){
                
            for (int i = 0; i < slength; i++) {
                System.out.println("nodeh");
                String x = (Integer.parseInt(inpCor[0])+i) + "," + inpCor[1];
                System.out.println(x);
                node[i] = (Integer.parseInt(inpCor[0])+i) + "," + inpCor[1];
            }
        }else{
            for (int i = 0; i < slength; i++) {
                System.out.println("nodev");
                String x = inpCor[0] + "," + (Integer.parseInt(inpCor[1])+i);
                System.out.println(x);
                node[i] = x;
            }
        }
    }
    
    @Override
    public String toString(){
        String out = "{" + sOrientation + ", (" + node[0] + "), (" + node[node.length-1] + ")}";
        return  out;
    }
}
