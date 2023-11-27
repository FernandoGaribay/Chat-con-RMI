package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    void receiveMessage(String message) throws RemoteException;
    void receivePrivateMessage(String sender, String message) throws RemoteException;
    void sendPrivateMessage(String receiver, String message) throws RemoteException;
    String getName() throws RemoteException; 
    String getIP() throws RemoteException;
}