package main;

import componentes.reciveMessage;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClientImpl extends java.rmi.server.UnicastRemoteObject implements ChatClient {

    private String name;
    private String clientIP;
    private ChatServer chatServer;
    private JPanel output;
    private JList listUsers;

    private static int BROADCAST_MESSAGE = 1234;
    private static int PRIVATE_MESSAGE = 9999;

    public ChatClientImpl(String name, String clientIP, ChatServer chatServer, JPanel output, JList listUsers) throws RemoteException {
        super();
        this.name = name;
        this.clientIP = clientIP;
        this.chatServer = chatServer;
        this.chatServer.registerClient(this);
        this.output = output;
        this.listUsers = listUsers;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        //System.out.println(message);
        output.add(new reciveMessage(message, false));
        output.repaint();
        output.revalidate();
    }

    @Override
    public void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException {
        ChatClient receiverInterface = chatServer.getReceiverInterface(receiver);
        String receiverIP = receiverInterface.getIP();

        if (receiverIP != null) {
            try {
                receiverInterface.iniciarCanalPrivado();
                privateMessage mir = (privateMessage) Naming.lookup("//" + receiverIP + ":" + PRIVATE_MESSAGE + "/privateMSG");
                mir.miMetodo1(sender, message);
            } catch (IOException ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
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

    public void sendMessage(String message) throws RemoteException {
        chatServer.broadcastMessage(this, name + ": " + message);
    }

    public void exitChat() throws RemoteException {
        chatServer.boardcastExitMessage(this, "-> " + name + " ha salido del chat.");
    }

    @Override
    public void iniciarCanalPrivado() {
        Thread socketThread = new Thread(() -> {
            try {
                privateMessage privateMessageObj = new privateMessageImpl();
                Registry registry = LocateRegistry.createRegistry(PRIVATE_MESSAGE);
                java.rmi.Naming.rebind("//"
                        + clientIP
                        + ":" + PRIVATE_MESSAGE + "/privateMSG", privateMessageObj);
                //System.out.println("servicio privado rmi iniciado");
            } catch (Exception ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        socketThread.start();
    }

    @Override
    public void updateClientList(List<ChatClient> clients) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> listModel = new DefaultListModel<>();

            for (ChatClient client : clients) {
                try {
                    listModel.addElement(client.getName());
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            listUsers.setModel(listModel);
        });
    }


    /* public static void main(String[] args) {
        try {
            String name = "ale";
            String clientIP = "192.168.1.88";
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
    } */
}
