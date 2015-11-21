package org.babax.somegame;

import org.babax.somegame.models.*;

import java.util.*;

import static org.babax.somegame.Util.getInt;

public class Engine {

    private String level;
    private Field field;
    public Graph graph;
    private Team team;

    private Point position;

    public Point keeper1_1, keeper1_2, keeper2_1, keeper2_2;

    public void init(List<String> params) {
        level = params.get(0);

        field = new Field();
        int width = getInt(params.get(1)) - 1;
        int length = getInt(params.get(2)) - 1;
        field.init(width, length);

        position = new Point();
        position.x = getInt(params.get(3));
        position.y = getInt(params.get(4));

        int trapsCount = getInt(params.get(5)) * 2;
        field.traps = parseTraps(params);

        int trapsShift = 5 + trapsCount + 1;
        keeper1_1 = new Point();
        keeper1_1.x = getInt(params.get(trapsShift));
        keeper1_1.y = getInt(params.get(trapsShift + 1));

        keeper1_2 = new Point();
        keeper1_2.x = getInt(params.get(trapsShift + 2));
        keeper1_2.y = getInt(params.get(trapsShift + 3));

        keeper2_1 = new Point();
        keeper2_1.x = getInt(params.get(trapsShift + 4));
        keeper2_1.y = getInt(params.get(trapsShift + 5));

        keeper2_2 = new Point();
        keeper2_2.x = getInt(params.get(trapsShift + 6));
        keeper2_2.y = getInt(params.get(trapsShift + 7));

        if(params.size() > trapsShift + 7) {
            System.out.println("WTF SASAI:" + params.size() + " " + trapsShift + 7);
        }
        graph = new Graph(field);
        if(width >= 500 && length >= 800) {
            graph.enable();
        }
        graph.markVisited(position);
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

    public void handleEnemyMove(Point to, Point keeper1, Point keeper2) {
        if(team == null)
            team = Team.SECOND;

        if(!field.isInField(to) || !field.isInField(keeper1) || !field.isInField(keeper2))
            return;

        if(team == Team.FIRST) {
            this.keeper2_1.x = keeper1.x;
            this.keeper2_1.y = keeper1.y;
            this.keeper2_2.x = keeper2.x;
            this.keeper2_2.y = keeper2.y;
        } else {
            this.keeper1_1.x = keeper1.x;
            this.keeper1_1.y = keeper1.y;
            this.keeper1_2.x = keeper2.x;
            this.keeper1_2.y = keeper2.y;
        }
        graph.trackMove(position, to);
        position = to;
    }

    public Team getTeam() {
        return team;
    }

    public Point findNextMove() {
        if(team == null)
            team = Team.FIRST;
        List<Point> keepers = Arrays.asList(keeper1_1, keeper1_2, keeper2_1, keeper2_2);
        EdgeEntry move = graph.findMove(position, getGate(), keepers);
        if(move != EdgeEntry.EMPTY) {
            Point result = move.getFirst();
            if(result != Point.NONE) {
                graph.trackMove(position, result);
                position = result;
            }
            return result;
        }
        return Point.NONE;
    }

    private Gate getGate() {
        return team == Team.FIRST? field.gate2 : field.gate1;
    }
}
