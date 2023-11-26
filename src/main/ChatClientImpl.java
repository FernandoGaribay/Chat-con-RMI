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

    public void sendPrivateMessage(String receiver, String message) throws RemoteException {
        chatServer.sendPrivateMessage(name, receiver, message);
    }

    public static void main(String[] args) {
        try {
            String name = "Fernando"; // args[0]

            // Especifica la dirección IP del servidor
            String serverIP = "192.168.1.87";

            // Crea el registro RMI apuntando a la dirección IP y el puerto del servidor
            Registry registry = LocateRegistry.getRegistry(serverIP, 1234);
            
            ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
            ChatClientImpl client = new ChatClientImpl(name, chatServer);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();

                if (message.startsWith("/private")) {
                    // Format: /private username message
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        client.sendPrivateMessage(parts[1], parts[2]);
                    } else {
                        System.out.println("Invalid private message format.");
                    }
                } else {
                    client.sendMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
