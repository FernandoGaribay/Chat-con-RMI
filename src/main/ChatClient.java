package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    void receiveMessage(String message) throws RemoteException;
    void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException;
    void iniciarCanalPrivado() throws RemoteException;
    String getName() throws RemoteException; 
    String getIP() throws RemoteException;
}