package main;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

public class privateMessageImpl extends UnicastRemoteObject implements privateMessage {

    public privateMessageImpl() throws RemoteException {
        super();
    }

    @Override
    public void miMetodo1(String sender, String message) throws RemoteException {
        //System.out.println("-> " + sender + " te ha susurrado: " + message);
        JOptionPane.showMessageDialog(null, message,"Mensaje privado de: " + sender, JOptionPane.WARNING_MESSAGE);
    }
}
