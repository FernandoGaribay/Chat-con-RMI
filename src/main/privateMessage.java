package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface privateMessage extends Remote {
    public void miMetodo1(String sender, String message) throws RemoteException;
}