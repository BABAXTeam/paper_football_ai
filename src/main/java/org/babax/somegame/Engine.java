package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;

import java.util.*;

public class Engine {

    private String level;
    private Field field;

    private Point position;

    public void init(List<String> params) {
        level = params.get(0);

        field = new Field();
        int width = getInt(params.get(1));
        int length = getInt(params.get(2));
        field.init(width, length);

        position = new Point();
        position.x = getInt(params.get(3));
        position.y = getInt(params.get(4));

        field.traps = parseTraps(params);
    }

    private Set<Point> parseTraps(List<String> params) {
        int trapsCount = getInt(params.get(5));
        if (trapsCount == 0)
            return Collections.emptySet();

        Set<Point> traps = new HashSet<>();
        for (int i = 6; i < params.size(); i += 2) {
            Point trap = new Point();
            trap.x = getInt(params.get(i));
            trap.y = getInt(params.get(i + 1));
            traps.add(trap);
        }
        return traps;
    }

    private static int getInt(String str) {
        return Integer.valueOf(str);
    }

    public Point findNextMove() {
        //TODO: replace stub
        return new Point(0, 0);
    }

    public void move(String x, String y) {
        position.x = getInt(x);
        position.y = getInt(y);
    }
}
