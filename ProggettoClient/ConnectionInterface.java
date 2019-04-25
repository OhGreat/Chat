/*
Implemented:    interface of the connection panel with actionlistener;
                methods to check ip and port
*/
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ConnectionInterface implements ActionListener{

    //GUI objects
    private JFrame frame;
    private JPanel mainPanel, userPanel, ipPanel, portPanel;
    private JTextField usernameField, ipField, portField;
    private JLabel setUser, setIP, setPort;
    private JButton connect;
    
    //additional utility objects
    private NewClientInterface nci;
    String ip, username; int port;
    
    
    public ConnectionInterface(NewClientInterface nci) {
        
        this.nci = nci;
        
        //creating invisible frame
        frame = new JFrame("Setup connection");
        mainPanel = new JPanel();
        userPanel = new JPanel(new BorderLayout()); ipPanel = new JPanel(new BorderLayout()); portPanel = new JPanel(new BorderLayout());
        usernameField = new JTextField(6); portField = new JTextField(4); ipField = new JTextField(8);
        setUser = new JLabel("set Username:"); setIP = new JLabel("set IP:"); setPort = new JLabel("set port:");
        connect = new JButton("Connect");
        connect.addActionListener(this);
        userPanel.add(setUser, BorderLayout.NORTH); userPanel.add(usernameField, BorderLayout.SOUTH);
        ipPanel.add(setIP, BorderLayout.NORTH); ipPanel.add(ipField, BorderLayout.SOUTH);
        portPanel.add(setPort, BorderLayout.NORTH); portPanel.add(portField, BorderLayout.SOUTH);
        mainPanel.add(ipPanel); mainPanel.add(portPanel); mainPanel.add(userPanel); mainPanel.add(connect);
        frame.add(mainPanel);
        
        //additional settings
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }  


    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equals("Connect")) {
            if (!usernameField.getText().equals("") && !ipField.getText().equals("") && !portField.getText().equals("")) {
                 if (validIP(ipField.getText()) && validPort(portField.getText())) {
                     port = Integer.parseInt(portField.getText()); ip = ipField.getText(); username = usernameField.getText();
                     try {
                         nci.setupConnection(ip, port, username);
                         frame.setVisible(false);
                     } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,"Cannot connecto to server");    
                     }
                 }else {
                    JOptionPane.showMessageDialog(null, "Invalid connection details");
                 }  
            }
            else {
            JOptionPane.showMessageDialog(null, "Invalid connection details");
            }
        }
        
    }
    

    //ip  pattern checking method
    public boolean validIP(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        ip = ip.trim();
        if ((ip.length() < 6) & (ip.length() > 15)) return false;

        try {
            Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        } catch (PatternSyntaxException ex) {
            return false;
        }
        
    }    
    
    
    //port checking method
    public boolean validPort(String port) {
        if (port == null || port.isEmpty()) {
            return false;
        }
        int portInteger = Integer.parseInt(port);
        if (portInteger < 1024 || portInteger > 65535) {
            return false;
        }
        return true;
        
    }

}