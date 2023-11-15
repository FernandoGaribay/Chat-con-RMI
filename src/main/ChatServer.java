package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void registerClient(ChatClient client) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException;
}