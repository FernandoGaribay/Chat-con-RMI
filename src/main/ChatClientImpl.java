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
        this.chatServer.registerClient(this);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void receivePrivateMessage(String sender, String message) throws RemoteException {
        System.out.println("Mensaje privado de " + sender + ": " + message);
    }

    @Override
    public void sendPrivateMessage(String receiver, String message) throws RemoteException {
        chatServer.sendPrivateMessage(this, receiver, message);
    }

    @Override
    public String getName() {
        return name;
    }
    
    private void sendMessage(String message) throws RemoteException {
        chatServer.broadcastMessage(this, name + ": " + message);
    }

    private void exitChat() throws RemoteException {
        chatServer.boardcastExitMessage(this, "-> " + name + " ha salido del chat.");
    }

    public static void main(String[] args) {
        try {
            //Nombre del cliente
            String name = "alex";
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

                if (message.startsWith("/private")) {
                    // Formato: /private [user] [message]
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        client.sendPrivateMessage(parts[1], parts[2]);
                    } else {
                        System.out.println("Error de comando privado");
                    }
                } else {
                    client.sendMessage(message);
                }

                exitCode = message.equalsIgnoreCase("/exit");
            }
            client.exitChat();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
