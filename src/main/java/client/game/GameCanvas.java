package client.game;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameCanvas extends Canvas implements Runnable {
    public CopyOnWriteArrayList<Cube> cubes;
    private boolean running;

    public GameCanvas() {
        cubes = new CopyOnWriteArrayList<>();
        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(800, 600));
        running = true;
    }

    public void addCube(Cube cube) {
        cubes.add(cube);
    }

    @Override
    public void paint(Graphics g) {
        // Floor
        g.setColor(Color.darkGray);
        g.fillRect(0, getHeight() - 200, getWidth(), 200);

        for (Cube cube : cubes) {
            cube.draw(g);
        }
    }

    @Override
    public void run() {
        while (running) {
            for (Cube cube : cubes) {
                cube.update();
            }
            repaint();

            try {
                Thread.sleep(16); // FPS
            } catch (InterruptedException e) {
                System.err.println("Game loop interrupted");
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }
}