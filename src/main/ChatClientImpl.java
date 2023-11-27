package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClientImpl extends java.rmi.server.UnicastRemoteObject implements ChatClient {

    private String name;
    private String clientIP;
    private ChatServer chatServer;

    private static int BROADCAST_MESSAGE = 1234;
    private static int PRIVATE_MESSAGE = 9999;

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
    public void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException {
        ChatClient receiverInterface = chatServer.getReceiverInterface(receiver);
        String receiverIP = receiverInterface.getIP();
        
        if (receiverIP != null) {
            try {
                receiverInterface.iniciarSocket();

                Socket serverSocket = new Socket(receiverIP, PRIVATE_MESSAGE);
                DataOutputStream outputServer = new DataOutputStream(serverSocket.getOutputStream());
                outputServer.writeUTF(sender + " te ha susurrado: " + message);
                serverSocket.close();

            } catch (IOException ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

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

    @Override
    public void iniciarSocket() {
        Thread socketThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PRIVATE_MESSAGE);
                Socket clientSocket;
                DataOutputStream outputClient;
                BufferedReader input;
                String message;

                clientSocket = serverSocket.accept(); // Acepta  al cliente
                outputClient = new DataOutputStream(clientSocket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while ((message = input.readLine()) != null) {
                    System.out.println(message);
                }
                clientSocket.close();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        socketThread.start();
    }

    public static void main(String[] args) {
        try {
            String name = "ale";
            String clientIP = "192.168.1.89";
            String serverIP = "192.168.1.87";

            Registry registry = LocateRegistry.getRegistry(serverIP, BROADCAST_MESSAGE);
            ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
            ChatClientImpl client = new ChatClientImpl(name, clientIP, chatServer);

            boolean exitCode = false;
            Scanner scanner = new Scanner(System.in);
            while (!exitCode) {
                String message = scanner.nextLine();

                if (message.startsWith("/msg")) {
                    // Formato: /private [user] [message]
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        client.sendPrivateMessage(name, parts[1], parts[2]);
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
