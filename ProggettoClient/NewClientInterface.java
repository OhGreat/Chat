/*
Implemented:    interface of main panel with actionlistener,
                method to setup connection with server,
                method main
*/
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class NewClientInterface implements ActionListener {

    //chat frame
    private JFrame frame;
    private JPanel panel, panel2;
    private JScrollPane chatPanel;
    private JTextArea messagesArea;
    private JTextField inputArea;
    private JButton buttSend;
    private String username;
     
    //JMenu Bar
    private JMenuBar menuBar;
    private JMenu connections;
    private JMenu fileTransfer;
    private JMenuItem newConnection;
    private JMenuItem disconnect;
    
    //additional objects
    ConnectionInterface connectionWindow;
    private LettoreFinestra windowListener;
    private Date currentDate;
    private SimpleDateFormat dateFormat;
    
    //networking
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    ClientInputStreamReader inputReader;
    
 
    public NewClientInterface() {
        
        //frame objects
        frame = new JFrame("Prive Chat v0.1");
        inputArea = new JTextField(42);
        inputArea.setText("Connect to a server first (Connect > New connection)");
        inputArea.setEditable(false);
        messagesArea = new JTextArea(20,50);
        messagesArea.setEditable(false);
        chatPanel = new JScrollPane(messagesArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttSend = new JButton("Send");
        buttSend.setEnabled(false);
        buttSend.addActionListener(this);
        
        //JMenu Bar
        menuBar =new JMenuBar();
        connections = new JMenu("Connect");
        fileTransfer = new JMenu("Torrent");
        newConnection = new JMenuItem("New connection");
        newConnection.addActionListener(this);
        disconnect = new JMenuItem("Disconnect");
        disconnect.setEnabled(false);
        disconnect.addActionListener(this);
        connections.add(newConnection);
        connections.add(disconnect);
        menuBar.add(connections);
        menuBar.add(fileTransfer);
        
        //packing
        frame.setJMenuBar(menuBar);
        panel = new JPanel(new BorderLayout());
        panel2 = new JPanel(new FlowLayout());
        panel2.add(inputArea);
        panel2.add(buttSend);
        panel.add(chatPanel,BorderLayout.CENTER);
        panel.add(panel2,BorderLayout.SOUTH);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //additional settings
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String cmd = e.getActionCommand();
        
        if (cmd.equals("Send")) {
            String message = inputArea.getText();
            if (!message.equals("")) {
                currentDate = new Date();
                String messaggioCompleto = "["+dateFormat.format(currentDate)+"]"+username+": "+message;
                try {
                    oos.writeObject(messaggioCompleto);
                    oos.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection with server lost");
                    
                    guiDisconnectedStatus();
                }
            }
            inputArea.setText("");
               
        }
        else if (cmd.equals("New connection")) {
            connectionWindow = new ConnectionInterface(this);   
        
        }
        else if (cmd.equals("Disconnect")) {
            try {
                oos.writeObject(new Integer(1));
                inputReader.stop();
                guiDisconnectedStatus();
               
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error while disconnecting from server, application dismissed");
                System.exit(1);
            }
        
        }
        else { 
            JOptionPane.showMessageDialog(null, "Fatal error, application dismissed");
            System.exit(1);
        }
      
    }
    
    //method to start connection to server
    public void setupConnection(String ip, int port, String username) throws IOException {
        this.username = username;
        socket = new Socket(ip,port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        
        String txt = messagesArea.getText();
        txt += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
        messagesArea.setText(txt);
        oos.writeObject(username);

        
        //starting information reception handler
        inputReader = new ClientInputStreamReader(this, socket, ois, oos, messagesArea);
        Thread inpR = new Thread(inputReader);
        inpR.start();
        windowListener = new LettoreFinestra(socket, ois, oos, inputReader);
        
        //gui connected status
        buttSend.setEnabled(true);
        inputArea.setText("");
        inputArea.setEditable(true);
        newConnection.setEnabled(false);
        disconnect.setEnabled(true);
        
    }
    
    //method that resets the gui to disconnected status
    public void guiDisconnectedStatus() {
        String txt = messagesArea.getText();
        txt += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
        messagesArea.setText(txt);
        newConnection.setEnabled(true);
        disconnect.setEnabled(false);
        buttSend.setEnabled(false);
        inputArea.setEditable(false);
        inputArea.setText("Connect to a server first (Connect > New connection)");
        
    }
    
    
    public static void main(String[] Args) {
        NewClientInterface ci = new NewClientInterface();
    }
    
}
    
   