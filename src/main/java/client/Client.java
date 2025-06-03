package client;

import client.game.Cube;
import client.game.GameCanvas;
import global.ConnectionData;
import global.Sender;
import global.ServerData;
import global.protocol.ChatMessage;
import global.protocol.ClientJoinMessage;
import global.protocol.ClientLeaveMessage;
import global.protocol.Message;
import global.protocol.central.GetServerRequestMessage;
import global.protocol.central.ServerFoundMessage;
import proxy.Proxy;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.Socket;

// TODO: uhh...maybe like rewrite because it seems we're approaching a world record number of malpractices in our code
public class Client {
    private static Server server;
    static String username;

    private static ConnectionData connectionData;

    static JTextArea chatArea;

    public static void main(String[] args) {
        openMainFrame();
    }

    // Our functions
    public static void startListen(String host, int port) {
        try {
            connectionData = new ConnectionData(new Socket(host, port));
            new Thread(
                    new ClientListener(connectionData)
            ).start();
            Sender.send(new ClientJoinMessage(username), connectionData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void startServer(String sName) {
        server = new Server(new ServerData(sName));
        new Thread(server).start();
    }

    // Like the game stuff (the actual server game windows)
    public static void startGameServer(String serverName) {
        JFrame gameFrame = new JFrame("YourBCAYourBCA - Server: " + serverName + "; Username: " + username);

        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(1000, 600);

        // Main panel
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.setBackground(new Color(44, 44, 44));

        GameCanvas canvas = new GameCanvas();
        canvas.addCube(new Cube(100, 100, 50, username, Color.RED));

        for (Cube cube : canvas.cubes) {
            cube.setAcceleration(0, 1);
            canvas.addCube(cube);
        }

        gameFrame.add(canvas);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);

        mainGamePanel.add(canvas, BorderLayout.CENTER);

        new Thread(canvas).start();

        gameFrame.setContentPane(mainGamePanel);

        // Chat stuff
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(300, 600));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setEnabled(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        JTextField chatInput = new JTextField();
        chatInput.setPreferredSize(new Dimension(300, 30));
        chatPanel.add(chatInput, BorderLayout.SOUTH);

        mainGamePanel.add(chatPanel, BorderLayout.EAST);

        gameFrame.setContentPane(mainGamePanel);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        chatInput.addActionListener((ActionEvent e) -> {
            String message = chatInput.getText().trim();
            chatArea.append(username +  ": " + message + "\n");
            if (!message.isEmpty()) {
                chatInput.setText("");
                if (connectionData == null) {
                    JOptionPane.showMessageDialog(gameFrame, "Connection not established!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Sender.send(new ChatMessage(message, username), connectionData);
            }
        });


        JButton leaveButton = new JButton("Leave");
        leaveButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leaveButton.setPreferredSize(new Dimension(100, 30));
        leaveButton.addActionListener(e -> {
            // TODO: Handle changing server ownership when the host leaves, for now doesn't kill server
            Sender.send(new ClientLeaveMessage(username), connectionData);
            gameFrame.dispose();
            openMainFrame();
        });

        JPanel leavePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leavePanel.setBackground(new Color(44, 44, 44));
        leavePanel.add(leaveButton);

        leavePanel.setBounds(mainGamePanel.getWidth() - chatArea.getWidth() - 120, 0, 120, 40);
        mainGamePanel.add(leavePanel);

        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                gameFrame.dispose();
                openMainFrame();
            }
        });
    }

    private static void openMainFrame() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } catch (Exception ex) {
                System.out.println("That didn't work");
            }

            // JFrame is like the window you're making
            JFrame frame = new JFrame("YourBCAYourBCA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // JPanel is a panel---an area within the frame in which you can add and control stuff
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(44, 44, 44));

            // JLabel is self-explanatory; you can set its font, border (margins), etc.
            JLabel welcomeLabel = new JLabel("YourBCAYourBCA!", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));
            mainPanel.add(welcomeLabel, BorderLayout.NORTH);

            // FlowLayout is a layout manager that tries its best to arrange the components based on the params
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 40));
            buttonPanel.setOpaque(false);

            // TODO: THESE BUTTONS SHOULD NOT BE USABLE WITHOUT A USERNAME PROMPT
            // ALSO TODO: MICHAEL EXPLAIN HOW THIS GUI THING WORKS SINCE I THINK A LOT IS GONNA BE HANGING ON IT

            // Button: It's a button
            JButton hostButton = new JButton("Host");
            JButton joinButton = new JButton("Join");

            // You can create fonts to reuse (as well as other things)
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 20);
            hostButton.setFont(buttonFont);
            joinButton.setFont(buttonFont);

            // Tries its best to keep the size constant here
            hostButton.setPreferredSize(new Dimension(140, 50));
            joinButton.setPreferredSize(new Dimension(140, 50));

            // If you hover over the button the small tooltip text will explain the button's purpose
            hostButton.setToolTipText("Host a new game");
            joinButton.setToolTipText("Join an existing game");

            // Adding basically an on click event to the button
            hostButton.addActionListener(e -> {

                // JTextField is a one-line text input component
                JTextField sNameField = new JTextField();
                JTextField yNameField = new JTextField();

                // Making a message object for what comes next
                Object[] message = {
                        "Server Name:", sNameField,
                        "Your Name: ", yNameField
                };

                // Creates that panel/dialog that pops up with; you can specify the frame it spawns above,
                // the message object array (like each row with the inputs), the title, and what kind of option icon
                // you want to appear (error, warning, the question mark thingy)
                int option = JOptionPane.showConfirmDialog(frame, message, "Start a Server", JOptionPane.OK_CANCEL_OPTION);

                // Logic portion
                if (option == JOptionPane.OK_OPTION) { // if you submit the form basically
                    String serverName = sNameField.getText(); // Gets from the text field
                    username = yNameField.getText();

                    System.out.println(username + " is now hosting " + serverName);

                    startServer(serverName); // Our function
                    startListen(Proxy.HOST, Proxy.PORT); // TODO: THIS BE REPLACED BY PROXY SERVER PLACE

                    // TODO: REPLACE THIS WITH JOINING THE SERVER AND BEHAVE AS NORMAL CLIENT FROM HEREIN
                    frame.dispose();
                    startGameServer(serverName);
                }
            });

            // Same as above but for the player join
            joinButton.addActionListener(e -> {
                JTextField nameField = new JTextField();
                JTextField usernameField = new JTextField();

                Object[] message = {
                        "Server name", nameField,
                        "Your Name: ", usernameField // This does nothing for now
                };
                int option = JOptionPane.showConfirmDialog(frame, message, "Join", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    username = usernameField.getText();

                    System.out.println(username);

                    int serverPort = -1;
                    try {
                        ConnectionData connectionData = new ConnectionData(new Socket(Proxy.HOST, Proxy.SHARER_PORT));
                        Sender.send(new GetServerRequestMessage(nameField.getText()), connectionData);
                        Message m = (Message) connectionData.getInput().readObject();
                        switch (m) {
                            case ServerFoundMessage found:
                                serverPort = found.port;
                            default:
                                // serverPort is already -1
                        }
                        String serverName = nameField.getText();
                        frame.dispose();
                        startGameServer(serverName);
                    } catch (Exception ex) {
                        System.err.println("UH OH");
                        ex.printStackTrace();
                    }
                    if (serverPort != -1)
                        startListen(Proxy.HOST, serverPort);

                    else {
                        // TODO
                    }
                }
            });

            // Dont forget to add the buttons and whatnot to the panel
            buttonPanel.add(hostButton);
            buttonPanel.add(joinButton);
            mainPanel.add(buttonPanel, BorderLayout.CENTER);

            frame.setContentPane(mainPanel); // Set the main container of the window
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}