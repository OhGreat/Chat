/*
Implemented:    ClientInputStreamReader implementing runnable methoud.
                Interrogates the server every 1 sec and expects replay as list of Strings
                method stop to for the runnable method
*/
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class ClientInputStreamReader implements Runnable{

    private NewClientInterface nci;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JTextArea messagesArea;
    
    private boolean running;
    
    public ClientInputStreamReader(NewClientInterface nci, Socket socket, ObjectInputStream ois, ObjectOutputStream oos, JTextArea messagesArea) {
        this.nci = nci;
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.messagesArea = messagesArea;
        running= false;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        
        running = true;
        while (running) {
            
            Object o = null;
            
            try {
                oos.writeObject(new Integer(0));
                oos.flush();
                o = ois.readObject();
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Connection with server lost");
                nci.guiDisconnectedStatus();
                stop();
                oos = null;
                ois = null;
		socket = null;
                break;
                
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Connection with server lost");
                nci.guiDisconnectedStatus();
                stop();
                oos = null;
                ois = null;
                socket = null;
                break;
                
            }
            
            List<String> lista = null;
            if (List.class.isInstance(o)) {
                lista = (List<String>) o;
            }
            String text = messagesArea.getText();
            Iterator<String> it = lista.iterator();
            while (it.hasNext()) {
                text +=  it.next()+"\n";
            }
            messagesArea.setText(text);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }    
        }
        
    }
    
    
    public void stop() {
        running = false;
    }
    
}
