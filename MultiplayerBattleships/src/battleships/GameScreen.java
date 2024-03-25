/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author dorianjacobs
 */
public class GameScreen extends JFrame{
    
    
    private JLabel output = new JLabel("Last Action");
    private JLabel ins = new JLabel("Instructions");
    private JTextField txtIn = new JTextField();        
    private JPanel p = new JPanel();
    JLabel background = new JLabel(new ImageIcon("backgroundGame.png"));
    JLabel title = new JLabel(new ImageIcon("title.png"));
    private JLabel cOut = new JLabel("Console...");
    private JLabel playerList = new JLabel("Players:");
    private  final JButton enterBtn = new JButton("Enter");
    private JLabel[] ships;
    private JLabel[] destroyedNodes;
    private int destroyedNodesCount = 0;
    private int shipsCount = 0;
    private BattleshipsClient client;
            
    public GameScreen(BattleshipsClient client){
        //this.setLocationRelativeTo();
        this.client = client;
        System.out.println("client in");
        this.add(p);
        p.setLocation(0, 0);

        this.getContentPane().setLayout(null);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setSize(1280, 720);
        this.setResizable(false);
        
        p.setSize(1280, 720);
        p.setLayout(null);
        
        p.add(background);
        p.add(title);
        p.add(txtIn);
        p.add(enterBtn);
        p.add(output);
        p.add(ins);
        p.add(cOut);
        p.add(playerList);

        background.setSize(1280, 720);
        background.setLocation(0, 0);

        title.setSize(880, 100);
        title.setLocation(200, 0);

        txtIn.setPreferredSize(new Dimension(100, 20));
        txtIn.setSize(100, 20);
        txtIn.setLocation(980, 640);
        txtIn.setVisible(true);
        
        enterBtn.setSize(100, 20);
        enterBtn.setLocation(980, 660);
        enterBtn.setFocusable(false);
        enterBtn.setBackground(Color.DARK_GRAY);
        enterBtn.setForeground(Color.WHITE);
        enterBtn.setVisible(true);
        
        enterBtn.addActionListener(new ActionListener() {
                @Override
                
                    public void actionPerformed(ActionEvent e) { 
                        
                        synchronized (enterBtn) {
                        enterBtn.notify();
                 
                    } 
        } 

                });
        
        output.setLocation(890, 100);
        output.setForeground(Color.WHITE);
        output.setPreferredSize(new Dimension(100, 100));
        output.setSize(200, 100);

        if(System.getProperty("os.name").contains("OS")){       
            output.setFont(new Font("Droid Sans Mono", Font.PLAIN, 14));
        }else{
            output.setFont(new Font("Droid Sans Mono", Font.PLAIN, 12));
        }

        output.setVisible(true);
        
        ins.setForeground(Color.WHITE);
        ins.setLocation(210, 600);
        ins.setPreferredSize(new Dimension(880, 60));
        ins.setSize(880, 60);
        ins.setFont(new Font("Droid Sans Mono", Font.PLAIN, 14));
        ins.setVisible(true);
        
        cOut.setLocation(1080, 0);
        cOut.setForeground(Color.WHITE);
        cOut.setPreferredSize(new Dimension(100, 100));
        cOut.setSize(200, 100);
        cOut.setFont(new Font("Droid Sans Mono", Font.PLAIN, 14));
        cOut.setVisible(true);
        
        playerList.setLocation(0, 0);
        playerList.setForeground(Color.WHITE);
        playerList.setPreferredSize(new Dimension(100, 100));
        playerList.setSize(200, 100);
        playerList.setFont(new Font("Droid Sans Mono", Font.PLAIN, 14));
        playerList.setVisible(true);

        p.setComponentZOrder(title, 0);
        p.setComponentZOrder(enterBtn, 1);
        p.setComponentZOrder(txtIn, 2);
        p.setComponentZOrder(ins, 3);
        p.setComponentZOrder(cOut, 4);
        p.setComponentZOrder(playerList, 5);
        p.setComponentZOrder(background, 6);

        p.setVisible(true);
        
        this.setVisible(true);
        
    }

    public String waitForEnter() {
        synchronized(enterBtn) {
            try {
                
                enterBtn.wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        //System.out.println("button clicked");
        
        return txtIn.getText();
    }
         
    public void updateDisplay(String s){
        output.setText(s);
        p.validate();
    }
    
    public String getOut(){
        return output.getText();
    }
    
    public String getTxtIn(){
        return txtIn.getText();
        
    }
    
    public String getConsole(){
        return cOut.getText();
    }
    
    public String getPlayerList(){
        return playerList.getText();
    }
    
    public void setPlayerList(String s){
        playerList.setText(s);
    }
    
    public void clearTextIn(){
        txtIn.setText("");
    }
    
    public String getIns(){
        return ins.getText();
    }
    
    public void updateInstruction(String s){
        ins.setText(s);
        p.validate();
    }
    
    public void updateConsole(String s){
        cOut.setText(s);
        p.validate();
    }
    public void setNodeNum(int l){
        destroyedNodes = new JLabel[l];
        for(int i = 0; i < ships.length; i++){
            destroyedNodes[i] = new JLabel();

        }
    }
    public void setShipNum(int l){
        ships = new JLabel[l];
        for(int i = 0; i < ships.length; i++){
            ships[i] = new JLabel();

        }
    }

    public void placeShip(int shipSize, int Orientation, int x, int y, int shipIndex){
        
        if(Orientation == 0){}
        switch(Orientation){
            case 0:
            switch(shipSize){
                case 1:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(50, 50);
                    ships[shipIndex].setIcon(new ImageIcon("singleNode.png"));
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    break;
                    
                case 2:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(100, 50);
                    ships[shipIndex].setIcon(new ImageIcon("doubleNode.png"));
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    break;
                    
                case 3:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(150, 50);
                    ships[shipIndex].setIcon(new ImageIcon("tripleNode.png"));
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    break;
                    
                case 4:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(200, 50);
                    ships[shipIndex].setIcon(new ImageIcon("quadNode.png"));
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    break;
                
                    
            }
            break;
            case 1:
            switch(shipSize){
                case 1:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(50, 50);
                    
                    ships[shipIndex].setIcon(new ImageIcon("singleNode.png"));
                    
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    
                    break;
                    
                case 2:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(50, 100);
                    
                    ships[shipIndex].setIcon(new ImageIcon("doubleNodeV.png"));
                    
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    
                    break;
                    
                case 3:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(50, 150);
                    
                    ships[shipIndex].setIcon(new ImageIcon("tripleNodeV.png"));
                    
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    
                    break;
                    
                case 4:
                
                    p.add(ships[shipIndex]);
                    ships[shipIndex].setSize(50, 200);
                    
                    ships[shipIndex].setIcon(new ImageIcon("quadNodeV.png"));
                    
                    ships[shipIndex].setLocation(390 + 50 * x, 100 + 50 * y);
                    
                    break;
                           
            }
            break;
        }
        
        ships[shipIndex].setVisible(true);
        p.setComponentZOrder(title, 0);
        p.setComponentZOrder(enterBtn, 1);
        p.setComponentZOrder(txtIn, 2);
        p.setComponentZOrder(ins, 3);
        p.setComponentZOrder(cOut, 4);
        p.setComponentZOrder(playerList, 5);
        for(int i = 0; i < shipIndex + 1; i++){
            p.setComponentZOrder(ships[i], 6 + i);
            
        }
        p.setComponentZOrder(background, 6 + shipIndex + 1);
        
        p.validate();
        
        shipsCount++;
    }

    public void destroyNode(int x, int y){
        p.add(destroyedNodes[destroyedNodesCount]);
        destroyedNodes[destroyedNodesCount].setSize(50, 50);
        destroyedNodes[destroyedNodesCount].setIcon(new ImageIcon("destroyedNode.png"));
        destroyedNodes[destroyedNodesCount].setLocation(390 + 50 * x, 100 + 50 * y);
        destroyedNodes[destroyedNodesCount].setVisible(true);
        for(int i = 0; i < destroyedNodesCount + 1; i++){
            p.setComponentZOrder(destroyedNodes[i],  i);
                        
        }
        for(int i = destroyedNodesCount; i < destroyedNodesCount + shipsCount; i++){
            p.setComponentZOrder(ships[i],  i);

        }
        p.setComponentZOrder(title, destroyedNodesCount + shipsCount + 1);
        p.setComponentZOrder(enterBtn, destroyedNodesCount + shipsCount + 2);
        p.setComponentZOrder(txtIn, destroyedNodesCount + shipsCount + 3);
        p.setComponentZOrder(ins, destroyedNodesCount + shipsCount + 4);
        p.setComponentZOrder(cOut, destroyedNodesCount + shipsCount + 5);
        p.setComponentZOrder(playerList, destroyedNodesCount + shipsCount + 6);
        p.setComponentZOrder(background, destroyedNodesCount + shipsCount + 7);
        p.validate();        
    }
    
    public void toEndScreen(){
        new EndScreen(client);
        client.EndScreen();
        this.dispose();
    }
    public JButton getButton(){
        return enterBtn;
        //panel3.validate();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_R:
                    playerList.setVisible(false);
            }
        }
    }
}
