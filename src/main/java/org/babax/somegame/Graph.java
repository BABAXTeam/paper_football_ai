package org.babax.somegame;

import org.babax.somegame.models.*;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.babax.somegame.models.Team.FIRST;

public class Graph {

    public Map<Long, Vertex> key2Vertex;
    public Set<Point> goals;

    public Field field;

    public Graph(Field field) {
        this.field = field;
        initNodes();
    }

    private void initNodes() {
        int capacity = field.width * field.length;
        key2Vertex = new HashMap<>(capacity);
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.width; x++) {
                Vertex v = initVertex(y, x);
                key2Vertex.put(genKey(x, y), v);
            }
        }
    }

    private Vertex initVertex(int y, int x) {
        Vertex v = new Vertex();
        v.x = x;
        v.y = y;

        v.edges = Stream.of(
                new Point(x - 1, y),
                new Point(x - 1, y - 1),
                new Point(x - 1, y + 1),
                new Point(x, y - 1),
                new Point(x, y + 1),
                new Point(x + 1, y),
                new Point(x + 1, y + 1),
                new Point(x + 1, y - 1)
        )
                .filter(point -> point.x >= 0
                              && point.y >= 0
                              && point.x <= field.width
                              && point.y <= field.length
                              && !field.isTrap(point)
                )
                .map(point -> new Edge(v, point))
                .collect(toSet());
        return v;
    }

    public void initGoal(Team team) {
        Gate gate = team == FIRST? field.gate1 : field.gate2;
        goals = new HashSet<>(gate.bottom.x - gate.top.x);
        int x = gate.top.x;
        do {
            Point p = new Point(x, gate.top.y);
            goals.add(p);
            x++;
        } while (x < gate.bottom.x);
    }

    public void findMove(Point from) {
        if(goals == null)
            throw new IllegalStateException("Goals can not be null");
    }

    private long genKey(int x, int y) {
        return (long)x << 32 | y & 0xFFFFFFFFL;
    }
}
