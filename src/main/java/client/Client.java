package client;

import client.game.Cube;
import client.game.GameCanvas;
import global.ConnectionData;
import global.GameType;
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
import server.game.Game;
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
    CardLayout cardLayout;
    JFrame gameFrame;
    JPanel mainGamePanel, centerPanel;

    public static void main(String[] args) {
        new Client().openMainFrame();
    }

    // Our functions
    public void startListen(String host, String serverName) {
        int serverPort = -1;
        try {
            ConnectionData serverSharerData = new ConnectionData(new Socket(Proxy.HOST, Proxy.SHARER_PORT));
            Sender.send(new GetServerRequestMessage(serverName), serverSharerData);
            Message m = (Message) serverSharerData.getInput().readObject();
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
            Sender.send(new ClientJoinMessage(username), connectionData);
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
    // In src/main/java/client/Client.java

    public void mainGameWindow(String serverName) {
        gameFrame = new JFrame("Cubes Game - Server: " + serverName + "; Username: " + username);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(1000, 600);
        gameFrame.setResizable(false);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(44, 44, 44));

        // CardLayout for center area like to swap the visibilities
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        // Waiting panel (with start button and icon)
        JPanel waitingPanel = new JPanel(null);
        waitingPanel.setBackground(new Color(44, 44, 44));

        JLabel waitingLabel = new JLabel("Waiting for host to start...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        waitingLabel.setForeground(Color.WHITE);
        waitingLabel.setBounds(150, 100, 400, 40);

        ImageIcon icon = new ImageIcon("src/main/java/images/jumpThumbnail.png");
        Image scaledImage = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setBounds(260, 180, 160, 120);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        startGameButton.setBounds(230, 320, 220, 60);

        boolean isHost = server != null;
        startGameButton.setEnabled(isHost);

        waitingPanel.add(waitingLabel);
        waitingPanel.add(iconLabel);
        waitingPanel.add(startGameButton);

        // Main game panel (canvas + join/leave)
        mainGamePanel = new JPanel(null);
        mainGamePanel.setBackground(new Color(44, 44, 44));

        canvas = new GameCanvas();
        cube = new Cube(username);
        canvas.setBounds(0, 0, 700, 600);
        mainGamePanel.add(canvas);

        JButton leaveButton = new JButton("Leave");
        leaveButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leaveButton.setBounds(20, 20, 120, 40);
        mainGamePanel.add(leaveButton);

        JButton joinButton = new JButton("Join Game");
        joinButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        joinButton.setBounds(20, 500, 130, 50);
        mainGamePanel.add(joinButton);

        mainGamePanel.setComponentZOrder(waitingPanel, 0);
        mainGamePanel.setComponentZOrder(leaveButton, 1);
        mainGamePanel.setComponentZOrder(joinButton, 2);

        mainGamePanel.setVisible(false);

        // Add cards to center panel
        centerPanel.add(waitingPanel, "waiting");
        centerPanel.add(mainGamePanel, "game");

        // Chat panel (always visible)
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(300, 600));
        chatPanel.setBackground(new Color(44, 44, 44));

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

        // Add panels to root
        rootPanel.add(centerPanel, BorderLayout.CENTER);
        rootPanel.add(chatPanel, BorderLayout.EAST);

        // Button actions
        startGameButton.addActionListener(e -> {
            server.startGame(GameType.JUMP_INCREMENTAL);
            beginGame(GameType.JUMP_INCREMENTAL);
        });

        joinButton.addActionListener(e -> {
            sendMessage(new GameRegisterMessage(username));
            sendMessage(new ClientShareStateMessage(username, cube.getPlayerData()));
            joinButton.setVisible(false);
            canvas.addCube(cube);
            inGame = true;
        });

        leaveButton.addActionListener(e -> {
            sendMessage(new ClientLeaveMessage(username));
            sendMessage(new GameUnregisterMessage(username));
            gameFrame.dispose();
            openMainFrame();
        });

        chatInput.addActionListener((ActionEvent e) -> {
            String message = chatInput.getText().trim();
            if (!message.isEmpty()) {
                chatArea.append(username + ": " + message + "\n");
                chatInput.setText("");
                if (connectionData == null) {
                    JOptionPane.showMessageDialog(gameFrame, "Connection not established!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Sender.send(new ChatMessage(message, username), connectionData);
            }
        });

        chatInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameFrame.requestFocus();
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

        gameFrame.setContentPane(rootPanel);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        SwingUtilities.invokeLater(() -> gameFrame.requestFocus());

        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                gameFrame.dispose();
                openMainFrame();
            }
        });
    }


    private void openMainFrame() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } catch (Exception ex) {
                System.out.println("That didn't work");
            }

            // JFrame is like the window you're making
            frame = new JFrame("Cubes Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // JPanel is a panel---an area within the frame in which you can add and control stuff
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(44, 44, 44));

            // JLabel is self-explanatory; you can set its font, border (margins), etc.
            JLabel welcomeLabel = new JLabel("Cubes Game!", SwingConstants.CENTER);
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
        if (server == null)
            Sender.send(m, connectionData);
        else
            server.handleMessage(m);
    }

    // theoretically would do whatever based on what the gametype is
    public void beginGame(GameType gameType) {
        cardLayout.show(centerPanel, "game");
        mainGamePanel.setVisible(true);
        new Thread(canvas).start();
        gameFrame.requestFocus();
    }
}