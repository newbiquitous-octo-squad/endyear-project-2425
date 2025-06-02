package client.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas {
    private List<Cube> cubes;

    public GameCanvas() {
        cubes = new ArrayList<>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
    }

    public void addCube(Cube cube) {
        cubes.add(cube);
    }

    @Override
    public void paint(Graphics g) {
        for (Cube cube : cubes) {
            cube.draw(g);
        }
    }
}