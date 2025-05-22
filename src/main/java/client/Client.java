package client;

import global.ConnectionData;

import java.io.IOException;
import java.net.Socket;

import javax.swing.*;

public class Client {
    public static void main(String[] args) throws IOException {

//        TODO: UNCOMMENT THIS ONCE THE SERVER SIDE IS FUNCTIONAL
//        new Thread(
//                new ClientListener(new ConnectionData(new Socket("localhost", 12345))) // obviously, change the host and port later
//        ).start();
        // TODO: GUI STUFF AND LIKE LITERALLY EVERYTHING ELSE

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } catch (Exception ex) {
                System.out.println("That didn't work");
            }
            JFrame frame = new JFrame("YourBCAYourBCA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setVisible(true);
        });
    }
}
