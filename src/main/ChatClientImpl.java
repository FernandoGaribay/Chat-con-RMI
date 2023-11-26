package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatClientImpl extends java.rmi.server.UnicastRemoteObject implements ChatClient {

    private String name;
    private ChatServer chatServer;

    protected ChatClientImpl(String name, ChatServer chatServer) throws RemoteException {
        super();
        this.name = name;
        this.chatServer = chatServer;
        chatServer.registerClient(this);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public void sendMessage(String message) throws RemoteException {
        chatServer.broadcastMessage(this, name + ": " + message);
    }
    
    public void exitChat() throws RemoteException {
        chatServer.boardcastExitMessage(this, "-> " + name + " ha salido del chat.");
    }

    public static void main(String[] args) {
        try {
            //Nombre del cliente
            String name = "Fernando";
            // IP del servidor
            String serverIP = "192.168.1.87";
            // Registro RMI con la direccion IP y el puerto del servidor
            Registry registry = LocateRegistry.getRegistry(serverIP, 1234);
            
            ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
            ChatClientImpl client = new ChatClientImpl(name, chatServer);

            boolean exitCode = false;  // Inicializar como false
            Scanner scanner = new Scanner(System.in);
            while (!exitCode) {
                String message = scanner.nextLine();
                client.sendMessage(message);

                exitCode = message.equalsIgnoreCase("/exit");
            }
            client.exitChat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
