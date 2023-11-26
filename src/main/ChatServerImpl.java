package main;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    private List<ChatClient> clients;

    protected ChatServerImpl() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public void registerClient(ChatClient client) throws RemoteException {
        clients.add(client);
    }

    @Override
    public void broadcastMessage(ChatClient sender, String message) throws RemoteException {
        for (ChatClient client : clients) {
            // Excluir al remitente del envío del mensaje
            if (!client.toString().equals(sender.toString())) {
                client.receiveMessage(message);
            }
        }
    }

    @Override
    public void boardcastExitMessage(ChatClient sender, String message) throws RemoteException {
        for (ChatClient client : clients) {
            if (!client.toString().equals(sender.toString())) {
                client.receiveMessage(message);
            }
        }
        clients.remove(sender);
    }

    public static void main(String[] args) {
        try {
            // Direccion ip (localhost = InetAddress.getLocalHost().getHostAddress();)
            String ipAddress = "192.168.1.87";

            // Establece la propiedad "java.rmi.server.hostname" para la dirección IP
            System.setProperty("java.rmi.server.hostname", ipAddress);

            // Crea el registro RMI en la dirección IP y el puerto especificados 1099
            Registry registry = LocateRegistry.createRegistry(1234);

            ChatServer chatServer = new ChatServerImpl();
            registry.rebind("ChatServer", chatServer);
            System.out.println("ChatServer ready at " + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
