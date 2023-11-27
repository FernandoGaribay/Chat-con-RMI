package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatClientImpl extends java.rmi.server.UnicastRemoteObject implements ChatClient {

    private String name;
    private String clientIP;
    private ChatServer chatServer;
    
    private static int BROADCAST_MESSAGE = 1234;
    private static int PRIVATE_MESSAGE = 1234;

    protected ChatClientImpl(String name, String clientIP, ChatServer chatServer) throws RemoteException {
        super();
        this.name = name;
        this.clientIP = clientIP;
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
        String receiverIP = chatServer.getReceiverIP(this, receiver);

        if (receiverIP != null) {
            // Aquí debes implementar la lógica para establecer una conexión directa
            // con receiverIP y enviar el mensaje
            System.out.println("Estableciendo conexión directa con " + receiverIP + " y enviando mensaje...");
        } else {
            System.out.println("Usuario '" + receiver + "' no encontrado o no está en línea.");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIP() throws RemoteException {
        return clientIP;
    }

    private void sendMessage(String message) throws RemoteException {
        chatServer.broadcastMessage(this, name + ": " + message);
    }

    private void exitChat() throws RemoteException {
        chatServer.boardcastExitMessage(this, "-> " + name + " ha salido del chat.");
    }

    public static void main(String[] args) {
        try {
            // Nombre del cliente
            String name = "alex";
            // IP del cliente
            String clientIP = "192.168.1.101";  // Cambia esto con la forma en que obtienes la IP del cliente
            // IP del servidor
            String serverIP = "192.168.1.87";
            // Registro RMI con la dirección IP y el puerto del servidor
            Registry registry = LocateRegistry.getRegistry(serverIP, BROADCAST_MESSAGE);

            ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
            ChatClientImpl client = new ChatClientImpl(name, clientIP, chatServer);

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

