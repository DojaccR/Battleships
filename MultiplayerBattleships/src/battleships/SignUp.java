package battleships;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Dorian Jacobs
 */
class SignUp extends JFrame{
    
    private JPanel p = new JPanel();
    
    private JLabel lblIns = new JLabel("Enter username ,password and IP address:");
    private JLabel lblUsername = new JLabel("Username:");
    private JLabel lblPassword = new JLabel("Password");
    
    
    private JTextField txtUsername = new JTextField();
    private JTextField txtPassword = new JTextField();
    
    
    private JCheckBox chkRegister = new JCheckBox("Register (Unchecked will assume sign in)");
    
    private JButton btnEnter = new JButton("Sign in");
    
    private BattleshipsClient client = null;

    public SignUp(BattleshipsClient client){
        this.client = client;
        this.setSize(400, 220);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Battleships Login");
        
        p.setSize(400, 220);
        p.setBackground(Color.blue);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        p.add(lblIns);
        lblIns.setForeground(Color.white);
        p.add(lblUsername);
        lblUsername.setForeground(Color.white);
        p.add(txtUsername);
        p.add(lblPassword);
        lblPassword.setForeground(Color.white);
        p.add(txtPassword);

        
        p.add(chkRegister);
        chkRegister.setBackground(Color.blue);
        chkRegister.setForeground(Color.white);
        
        btnEnter.addActionListener(new ActionListener() {
            @Override
                
                public void actionPerformed(ActionEvent e) { 
                    synchronized (btnEnter) {
                    btnEnter.notify();
                 
                } 
            }             
        });
        p.add(btnEnter);
        btnEnter.setBackground(Color.darkGray);
        p.setVisible(true);
        
        this.add(p);
        this.setVisible(true);
         
    }
    
    public void setUsernamelbl(String s){
        lblUsername.setText(s);
    }
    
    public void setPasswordlvl(String s){
        lblPassword.setText(s);
    }
    
    public void end(){
        this.dispose();
    }

    public void toMain(){
        

        
        client.GameScreenComs(new GameScreen(client));
        this.dispose();
        //new MainScreen(client);
        //this.dispose();

    }
    
    public void setInstruction(String s){
        lblIns.setText(s);
        
    }
    public String waitForEnter() {
        synchronized(btnEnter) {
            try {
                
                btnEnter.wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        //System.out.println("button clicked");
        if(chkRegister.isSelected()){
            return "register#" + txtUsername.getText() + "#" + txtPassword.getText();
        }else{
            return "signIn#" + txtUsername.getText() + "#" + txtPassword.getText();
        }
        
    }

    void clearTextIn() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    
    
    
}
