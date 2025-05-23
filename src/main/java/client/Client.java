package client;

import global.ConnectionData;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class Client {
    private static Server server;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } catch (Exception ex) {
                System.out.println("That didn't work");
            }
            JFrame frame = new JFrame("YourBCAYourBCA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(44, 44, 44));

            JLabel welcomeLabel = new JLabel("YourBCAYourBCA!", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));
            mainPanel.add(welcomeLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 40));
            buttonPanel.setOpaque(false);

            JButton hostButton = new JButton("Host");
            JButton joinButton = new JButton("Join");

            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 20);
            hostButton.setFont(buttonFont);
            joinButton.setFont(buttonFont);

            hostButton.setPreferredSize(new Dimension(140, 50));
            joinButton.setPreferredSize(new Dimension(140, 50));

            hostButton.setToolTipText("Host a new game (coming soon)");
            joinButton.setToolTipText("Join an existing game");

            hostButton.addActionListener(e -> {
                // TODO: PROMPT USER FOR NAME & PORT
                server = new Server(12345, "John Doe's Game");
                new Thread(server).start();
            });

            joinButton.addActionListener(e -> {
                JTextField hostField = new JTextField();
                JTextField portField = new JTextField();
                Object[] message = {
                        "Host:", hostField,
                        "Port:", portField
                };
                int option = JOptionPane.showConfirmDialog(frame, message, "Join", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String host = hostField.getText();
                    int port;
                    try {
                        port = Integer.parseInt(portField.getText());
                        startListen(host, port);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            buttonPanel.add(hostButton);
            buttonPanel.add(joinButton);
            mainPanel.add(buttonPanel, BorderLayout.CENTER);

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void startListen(String host, int port) {
        try {
            new Thread(
                    new ClientListener(new ConnectionData(new Socket(host, port)))
            ).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}