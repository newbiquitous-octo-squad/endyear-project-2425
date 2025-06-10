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
import global.protocol.game.GameRegisterMessage;
import global.protocol.game.GameUnregisterMessage;
import global.protocol.game.jumpincremental.ClientJumpMessage;
import global.protocol.game.jumpincremental.ClientShareStateMessage;
import proxy.Proxy;
import server.Server;
import server.game.jumpincremental.JumpIncremental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private Server server;
    String username;
    private boolean inGame = false;
    private Timer timer = new Timer();
    private JFrame frame;
    private Cube cube;

    private ConnectionData connectionData;

    JTextArea chatArea;
    GameCanvas canvas;

    public static void main(String[] args) {
        new Client().openMainFrame();
    }

    // Our functions
    public void startListen(String host, String serverName) {
        int serverPort = -1;
        try {
            connectionData = new ConnectionData(new Socket(Proxy.HOST, Proxy.SHARER_PORT));
            sendMessage(new GetServerRequestMessage(serverName));
            Message m = (Message) connectionData.getInput().readObject();
            switch (m) {
                case ServerFoundMessage found:
                    serverPort = found.port;
                    break;
                default:
                    // serverPort still -1
            }
        } catch (Exception ex) {
            System.err.println("Error while connecting to server");
            ex.printStackTrace();
        }

        if (serverPort == -1) {
            JOptionPane.showMessageDialog(frame, "Server not found. Please check the server name and try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            connectionData = new ConnectionData(new Socket(host, serverPort));
            new Thread(
                    new ClientListener(connectionData, this)
            ).start();
            sendMessage(new ClientJoinMessage(username));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void startServer(String sName) {
        server = new Server(new ServerData(sName));
        new Thread(server).start();
    }

    public void stopServer() {
        server.stop();
    }

    // Like the game stuff (the actual server game windows)
    public void mainGameWindow(String serverName) {
        JFrame gameFrame = new JFrame("YourBCAYourBCA - Server: " + serverName + "; Username: " + username);

        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(1000, 600);

        // Main panel
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.setBackground(new Color(44, 44, 44));

        canvas = new GameCanvas();
        cube = new Cube(username);

        for (Cube c : canvas.cubes) {
            c.setAcceleration(0, 1);
            canvas.addCube(c);
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
                sendMessage(new ChatMessage(message, username));
            }
        });


        JButton leaveButton = new JButton("Leave");
        leaveButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leaveButton.setPreferredSize(new Dimension(100, 30));
        leaveButton.addActionListener(e -> {
            // TODO: Handle changing server ownership when the host leaves, for now doesn't kill server
            sendMessage(new ClientLeaveMessage(username));
            sendMessage(new GameUnregisterMessage(username));
            gameFrame.dispose();
            openMainFrame();
        });

        JPanel leavePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leavePanel.add(leaveButton);

        mainGamePanel.setLayout(null);

        canvas.setBounds(0, 0, mainGamePanel.getWidth() - chatPanel.getWidth(), mainGamePanel.getHeight());

        leavePanel.setBounds(10, 10, 108, 40);
        mainGamePanel.add(leavePanel);

        // Join the inside game button
        JButton joinButton = new JButton("Join Game");
        joinButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        joinButton.setPreferredSize(new Dimension(120, 40));

        JPanel joinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        joinPanel.add(joinButton);

        joinPanel.setBounds(10, mainGamePanel.getHeight() - 55, 130, 50);
        mainGamePanel.add(joinPanel);

        joinButton.addActionListener(e -> {
            sendMessage(new GameRegisterMessage(username));
            sendMessage(new ClientShareStateMessage(username, cube.getPlayerData()));
            joinPanel.setVisible(false);
            canvas.addCube(cube);
            inGame = true;
        });


        mainGamePanel.setComponentZOrder(leavePanel, 0);
        mainGamePanel.setComponentZOrder(joinPanel, 1);
        mainGamePanel.setComponentZOrder(canvas, 2);

        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                gameFrame.dispose();
                openMainFrame();
            }
        });

        chatInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        gameFrame.requestFocus();
                        break;
                }
            }
        });

        gameFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!inGame) return;

                if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                    chatInput.requestFocus();
                } else if (!chatInput.isFocusOwner()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            if (cube.getY() + cube.getHeight() == JumpIncremental.FLOOR_HEIGHT) {
                                cube.setVelocity((int) cube.getPlayerData().velocityX, -40);
                                sendMessage(new ClientJumpMessage(username));
                            }
                            break;
                        case KeyEvent.VK_A:
                            cube.setVelocity(-30, (int) cube.getPlayerData().velocityY);
                            break;
                        case KeyEvent.VK_D:
                            cube.setVelocity(30, (int) cube.getPlayerData().velocityY);
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!chatInput.isFocusOwner()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_D:
                            cube.setVelocity(0, (int) cube.getPlayerData().velocityY);
                            break;
                    }
                }
            }
        });

        SwingUtilities.invokeLater(() -> gameFrame.requestFocus());
    }

    private void openMainFrame() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } catch (Exception ex) {
                System.out.println("That didn't work");
            }

            // JFrame is like the window you're making
            frame = new JFrame("YourBCAYourBCA");
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
                JTextField sNameField = new JTextField();
                JTextField yNameField = new JTextField();

                Object[] message = {
                        "Server Name:", sNameField,
                        "Your Name: ", yNameField,
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Start a Server", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String serverName = sNameField.getText();
                    username = yNameField.getText();

                    try {
                        startServer(serverName);
                        Thread.sleep(100); // Ensure server starts before listening
                        startListen(Proxy.HOST, serverName);

                        if (connectionData != null && connectionData.getSocket().isConnected()) {
                            frame.dispose();
                            mainGameWindow(serverName);
                        } else {
                            // Something
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error while starting the server.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            // Same as above but for the player join
            joinButton.addActionListener(e -> {
                JTextField nameField = new JTextField();
                JTextField usernameField = new JTextField();

                Object[] message = {
                        "Server name", nameField,
                        "Your Name: ", usernameField
                };
                int option = JOptionPane.showConfirmDialog(frame, message, "Join", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    username = usernameField.getText();
                    String serverName = nameField.getText();

                    try {
                        startListen(Proxy.HOST, serverName);

                        if (connectionData != null && connectionData.getSocket().isConnected()) {
                            frame.dispose();
                            mainGameWindow(serverName);
                        } else {
                            // Something
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error while connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
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
    public void startJumpIncrementalTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new ClientShareStateMessage(username, cube.getPlayerData()));
            }
        }, 63, JumpIncremental.TICK_DELAY);
    }

    public boolean isInGame() {
        return inGame;
    }

    public synchronized void sendMessage(Message m) {
        Sender.send(m, connectionData);
    }
}