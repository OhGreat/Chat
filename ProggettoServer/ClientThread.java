/*
Implemented:    ClientThread runnable method that handles client requests,
                method to add messages from server to client thread list,
                method to send messages list from clientThread to thread,
                method to close communication with server
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class ClientThread implements Runnable {

    private Server server;
    private ServerInterface serverInterface;
    private boolean fired = false, running = true;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private List<String> newMessagesList;
    private String name = null;
    
    
    public ClientThread(Socket socket, Server server,ServerInterface serverInterface) {
        this.socket = socket;
        this.server = server;
        this.serverInterface = serverInterface;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());    
        } catch (IOException ex) {
                fired = true;
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
                fired = true;
            }
        }
        newMessagesList = new LinkedList<String>();
    }
    
    
    @Override
    public void run() {
        
        running = true;
        if (fired) { return; }
        fired = true;
        
        while (running) {
            
            try {
                Object o = ois.readObject();
                if (Integer.class.isInstance(o)) {
                    int cmd = (int) o;
                    
                    if (cmd == 1) {
                        running = false;
                        close();
                    }
                    if (cmd == 0) {
                        sendMessages();
                    }
                }
                
                if (String.class.isInstance(o)) {
                    if (name == null) {
                        name = (String) o;
                        serverInterface.addConsoleTxt("user '"+name+"' has joined the chat");
                        server.forwardMessage(" user '"+name+"' has joined the chat");
                    }
                    else{
                        String messaggioCompleto = (String) o;
                        server.forwardMessage(messaggioCompleto); 
                    }
                }    
            } catch (IOException ex) {
                running = false;
                try {
                    close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            } catch (ClassNotFoundException ex) {
                running = false;
                try {
                    close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            } 
        }
        
    }
    
    
    //method the server invoces to add messages to the clientThread list
    public void addMessaggio(String messaggioCompleto) {
        newMessagesList.add(messaggioCompleto);
    }
    
    
    //method the clientThread uses to send messages to the client
    public void sendMessages() {
        try {
            oos.writeObject(newMessagesList);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        newMessagesList = new LinkedList<String>();
    }
    
    
    //method that closes communication with client
    public void close() throws IOException {
        ois.close();
        oos.close();
        socket.close();
        serverInterface.addConsoleTxt("User '"+name+"' has disconnected");
        try {
            server.forwardMessage("User '"+name+"' has disconnected");
        }catch(NullPointerException ex) {}
    }
    
    
    public boolean isCLosed() {
        return socket.isClosed();
    }
    
}
