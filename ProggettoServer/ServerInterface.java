/*
Implemented:    server interface, action listener, method to add text to console, method main 
*/
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ServerInterface implements ActionListener {

    private JFrame frame;
    private JPanel panel, insidePanel;
    private JTextArea console;
    private String consoletxt;
    private JTextField portField;
    private JLabel portLabel;
    private JButton buttStart, buttStop;
    private Server server;
    private JScrollPane consoleScroll;
    
    
    public ServerInterface() {
         
        frame = new JFrame("Server Broadcaster");
        panel = new JPanel(new BorderLayout());
        insidePanel = new JPanel(new FlowLayout());
        buttStart = new JButton("Start");
        buttStart.addActionListener(this);
        buttStop = new JButton("Stop");
        buttStop.addActionListener(this);
        buttStop.setEnabled(false);
        portField = new JTextField(4);
        portField.setText("4500");
        portLabel = new JLabel("Insert port: ");
        console = new JTextArea(10,20);
        console.setEditable(false);
        consoleScroll = new JScrollPane(console,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        consoletxt = "";
        insidePanel.add(portLabel);
        insidePanel.add(portField);
        insidePanel.add(buttStart);
        insidePanel.add(buttStop);
        panel.add(consoleScroll, BorderLayout.CENTER);
        panel.add(insidePanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String cmd = e.getActionCommand();
        
        if (cmd.equals("Start")) {
            setupConnection();
            
        }
        else if (cmd.equals("Stop")) {
            buttStop.setEnabled(false);
            server.stop();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            buttStart.setEnabled(true);
            addConsoleTxt("Server stopped successfully");
        
        }
        else { System.exit(1); }
        
    }
    
    
    //server connection method
    public void setupConnection() {
        
        if (!portField.getText().equals("")) {
            try {
                int port = Integer.parseInt(portField.getText());
                if (port > 1024 && port < 65535) {
                    server = new Server(this,port);
                    buttStart.setEnabled(false);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    Thread th = new Thread(server);
                    th.start();
                    addConsoleTxt("Server started successfully"); 
                    try {
                        String ip = InetAddress.getLocalHost().toString();
                        addConsoleTxt("ip: "+ip);
                        addConsoleTxt("port: "+port);
                    } catch (UnknownHostException ex) {
                        addConsoleTxt("ip address can't be determinedS");
                    }
                    buttStop.setEnabled(true);
                }
                else { 
                    JOptionPane.showMessageDialog(null, "Invalid port"); 
                } 
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port");
            }
        }
        else { JOptionPane.showMessageDialog(null,"Port required to start server"); }
        
    }

    //method to add text to the server console
    public void addConsoleTxt(String newtxt) {
        console.setText(console.getText()+newtxt+"\n");
    }
    
    
    public static void main(String[] Args) {
        ServerInterface si = new ServerInterface();
    }
    
}
