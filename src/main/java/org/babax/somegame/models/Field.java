package org.babax.somegame.models;

import java.util.List;

public class Field {

    public int width;
    public int length;
    public List<Point> traps;

    public Gate gate1;
    public Gate gate2;

    public void init(int width, int length) {
        this.width = width;
        this.length = length;
        matchGates(width, length);
    }

    private void matchGates(int width, int length) {
        int k = width / 5;
        gate1 = new Gate();
        gate1.top = new Point(2 * k, 0);
        gate1.bottom = new Point(3 * k, 0);

        gate2 = new Gate();
        gate2.top = new Point(2 * k, length);
        gate2.bottom = new Point(3 * k, length);
    }
}
