/*
Implemented:    windowlistener interface for NewClientInterface main panel,
                method to return integer(1) when window is closing
*/
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class LettoreFinestra implements WindowListener {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientInputStreamReader clInput;

    
    public LettoreFinestra(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, ClientInputStreamReader clInput) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.clInput = clInput; 
    }
  
    
    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public void windowClosing(WindowEvent e) {
        try {
            oos.writeObject(new Integer(1));
            clInput.stop();
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    
    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
