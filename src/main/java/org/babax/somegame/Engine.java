package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;
import org.babax.somegame.models.Team;

import java.util.*;

import static org.babax.somegame.Util.getInt;

public class Engine {

    private String level;
    private Field field;
    private Graph graph;
    private Team team;

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

        graph = new Graph(field);
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

    public Point findNextMove() {
        //TODO: replace stub
        return new Point(0, 0);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void handleEnemyMove(Point to) {
        if(team == null)
            team = Team.SECOND;
        graph.markDisabled(position, to);
        position = to;
    }

    public Team getTeam() {
        return team;
    }
}
