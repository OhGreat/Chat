/*
Implemented:    runnable method that opens a ServerSocket and continiously accepts new connections,
                creates the ClientThread in a new thread and saves its istance in a list,
                Implemented methods to stop serverSocket and close all connections,
                method to add messages to ClientThread list
*/
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;


public class Server implements Runnable{
    
    //GUI creation
    private ServerSocket serverSocket;
    private boolean flag;
    private List<ClientThread> lista;
    private ServerInterface serverInterface;
    private int port;
    
    public Server(ServerInterface serverInterface, int port) {
        this.serverInterface = serverInterface;
        this.port = port;
    }
    
    
    @Override
    public void run() {
        
        if (!flag) {
            flag = true;
        
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"Error while creating ServerSocket");
                System.exit(1);
            }
            
            Socket socket;
            lista = new LinkedList<ClientThread>();
            
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException ex) {
                    break;
                }
                ClientThread cl = new ClientThread(socket,this,serverInterface);
                Thread clThread = new Thread(cl);
                clThread.start();
                lista.add(cl);
            }
        }
    }
    
    
    //method to stop the serverSocket
    public void stop() {
        if (flag) {
            flag = false;
            try {
                serverSocket.close();
            } catch (IOException ex) {
                System.exit(1);
            }
            
            if (!closeConnections()) {
                System.exit(1);
            }
        }
    }
    
    
    //method to close connections
    public boolean closeConnections() {
        Iterator<ClientThread> it = lista.iterator();
        while (it.hasNext()) {
            ClientThread cl = it.next();
            if (!cl.isCLosed()) {
                try {
                    cl.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } 
        }
        lista = null;
        return true;
    }
    
    
    //method to forward messages
    public void forwardMessage(String messaggioCompleto) {
        Iterator<ClientThread> it = lista.iterator();
        while (it.hasNext()) {
            ClientThread cl = it.next();
            cl.addMessaggio(messaggioCompleto);
        }
    }
    
}
