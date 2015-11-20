package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Engine {

    private String level;
    private Field field;

    private Point myPosition;
    private Point enemyPosition;

    public void init(List<String> params) {
        level = params.get(0);

        field = new Field();
        int width = getInt(params.get(1));
        int length = getInt(params.get(2));
        field.init(width, length);

        myPosition = new Point();
        myPosition.x = getInt(params.get(3));
        myPosition.y = getInt(params.get(4));

        field.traps = parseTraps(params);
    }

    private List<Point> parseTraps(List<String> params) {
        int trapsCount = getInt(params.get(5));
        if(trapsCount == 0)
            return Collections.emptyList();

        List<Point> traps = new ArrayList<>();
        for(int i = 6; i < params.size(); i += 2) {
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
}
