package main;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
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
    public void broadcastMessage(String message) throws RemoteException {
        for (ChatClient client : clients) {
            client.receiveMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(String sender, String receiver, String message) throws RemoteException {
        for (ChatClient client : clients) {
            if (client.toString().equals(receiver)) {
                client.receiveMessage(sender + " (private): " + message);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Obtén la dirección IP de la máquina actual
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            
            // Establece la propiedad "java.rmi.server.hostname" para la dirección IP
            System.setProperty("java.rmi.server.hostname", ipAddress);

            // Crea el registro RMI en la dirección IP y el puerto especificados
            Registry registry = LocateRegistry.createRegistry(1099);

            ChatServer chatServer = new ChatServerImpl();
            registry.rebind("ChatServer", chatServer);
            System.out.println("ChatServer ready at " + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
