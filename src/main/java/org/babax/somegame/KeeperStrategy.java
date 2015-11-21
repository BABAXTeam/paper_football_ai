package org.babax.somegame;

import org.babax.somegame.models.Gate;
import org.babax.somegame.models.Point;

public class KeeperStrategy {

    private Gate gate;
    private Graph graph;

    public KeeperStrategy(Gate gate, Graph graph) {
        this.gate = gate;
    }

    public boolean handleMove(Point p, Point keepeer1, Point keeper2) {

        return false;
    }

}
