package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void registerClient(ChatClient client) throws RemoteException;
    void broadcastMessage(ChatClient sender, String message) throws RemoteException;
    void boardcastExitMessage(ChatClient sender, String message) throws RemoteException;
}