package UI;

import componentes.reciveMessage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.ChatClientImpl;
import main.ChatServer;

public class clientUI extends javax.swing.JFrame {

    private ChatClientImpl objChatClient;
    private String name;
    private String clientIP;
    private String serverIP;

    private static int BROADCAST_MESSAGE = 1234;
    private static int PRIVATE_MESSAGE = 9999;

    public clientUI() {
        initComponents();
//        this.name = JOptionPane.showInputDialog("Ingrese su nombre:");
//        this.clientIP = JOptionPane.showInputDialog("Ingrese la dirección IP del cliente:");
//        this.serverIP = JOptionPane.showInputDialog("Ingrese la dirección del servidor de chat:");

        this.name = "ale";
        this.clientIP = "192.168.1.89";
        this.serverIP = "192.168.1.87";

        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, BROADCAST_MESSAGE);
            ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
            objChatClient = new ChatClientImpl(name, clientIP, chatServer, pnlChat, listUsers);
        } catch (Exception ex) {
            Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        scrollUsuariosConectados = new javax.swing.JScrollPane();
        listUsers = new javax.swing.JList<>();
        lblUsuariosConectados = new javax.swing.JLabel();
        textMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        scrollChat = new javax.swing.JScrollPane();
        pnlChat = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(910, 600));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(255, 255, 255));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listUsers.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        listUsers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listUsersValueChanged(evt);
            }
        });
        scrollUsuariosConectados.setViewportView(listUsers);

        background.add(scrollUsuariosConectados, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 50, 183, 530));

        lblUsuariosConectados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuariosConectados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsuariosConectados.setText("Usuarios conectados");
        background.add(lblUsuariosConectados, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 180, 20));

        textMessage.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        background.add(textMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 532, 530, 50));

        btnSend.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        btnSend.setText("->");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        background.add(btnSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 530, 110, 50));

        scrollChat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlChat.setBackground(new java.awt.Color(248, 248, 248));
        pnlChat.setLayout(new javax.swing.BoxLayout(pnlChat, javax.swing.BoxLayout.PAGE_AXIS));
        scrollChat.setViewportView(pnlChat);

        background.add(scrollChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 650, 500));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 910, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        if (!textMessage.getText().isEmpty()) {
            try {
                if(!textMessage.getText().startsWith("/msg")){
                    pnlChat.add(new reciveMessage(textMessage.getText(), true));
                    pnlChat.repaint();
                    pnlChat.revalidate();
                }

                if (textMessage.getText().startsWith("/msg")) {
                    // Formato: /private [user] [message]
                    String[] parts = textMessage.getText().split(" ", 3);
                    if (parts.length == 3) {
                        objChatClient.sendPrivateMessage(name, parts[1], parts[2]);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error en la sintaxis de mensaje privado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    objChatClient.sendMessage(textMessage.getText());
                }

                if (textMessage.getText().equalsIgnoreCase("/exit")) {
                    objChatClient.exitChat();
                    System.exit(0);
                }

                textMessage.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void listUsersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listUsersValueChanged
        if (!evt.getValueIsAdjusting()) {
            int selectedIndex = listUsers.getSelectedIndex();
            String selectedName = listUsers.getModel().getElementAt(selectedIndex);
            if (selectedIndex != -1 && !selectedName.equals(objChatClient.getName())) {
                System.out.println(selectedName);
                System.out.println(objChatClient.getName());
                textMessage.setText("/msg " + selectedName + " ");
                textMessage.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "No puedes mandarte un mensaje privado a ti mismo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_listUsersValueChanged

    public static void main(String args[]) {
        // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(clientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(clientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(clientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(clientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new clientUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblUsuariosConectados;
    private javax.swing.JList<String> listUsers;
    private javax.swing.JPanel pnlChat;
    private javax.swing.JScrollPane scrollChat;
    private javax.swing.JScrollPane scrollUsuariosConectados;
    private javax.swing.JTextField textMessage;
    // End of variables declaration//GEN-END:variables
}
