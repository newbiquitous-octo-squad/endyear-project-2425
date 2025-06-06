package client.game;

import server.game.jumpincremental.JumpIncremental;

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
    }

    @Override
    public void update(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight() - 200);

        for (Cube cube : cubes) {
            cube.draw(g);
        }
    }

    @Override
    public void run() {
        while (running) {
            for (Cube cube : cubes) {
                cube.getPlayerData().update();
            }
            repaint();

            try {
                Thread.sleep(JumpIncremental.TICK_DELAY);
            } catch (InterruptedException e) {
                System.err.println("Game loop interrupted");
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }
    public void start() {
        running = true;
    }
}