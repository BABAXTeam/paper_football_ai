package org.babax.somegame;

import org.babax.somegame.models.*;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Graph {

    public Map<Long, Vertex> key2Vertex;

    public Field field;

    public Graph(Field field) {
        this.field = field;
        initNodes();
    }

    private void initNodes() {
        int capacity = field.width * field.length;
        key2Vertex = new HashMap<>(capacity);
        for (int y = 0; y <= field.length; y++) {
            for (int x = 0; x <= field.width; x++) {
                Vertex v = initVertex(x, y);
                key2Vertex.put(genKey(x, y), v);
            }
        }
    }

    private Vertex initVertex(int x, int y) {
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
        ).filter(point -> field.isInField(point)
                && !((x == point.x && field.isBorder(v) && field.isBorder(point))|| (y == point.y && field.isBorder(v) && field.isBorder(point))))
/*                .filter(point -> point.x >= 0
                                && point.y >= 0
                                && point.x <= field.width
                                && point.y <= field.length
                                && !(field.isBorder(x, y) && field.isBorder(point))
                                && !field.isTrap(point)
                )*/
                .map(point -> new Edge(v, point))
                .collect(toSet());
        return v;
    }

    public boolean findMove(Point from, Gate gate) {
        Vertex fromV = key2Vertex.get(genKey(from));

        PriorityQueue<EdgeEntry> priorityQueue = new PriorityQueue<>();

        Map<Long, EdgeEntry> bestWays = new HashMap<>();
        EdgeEntry initial = new EdgeEntry();
        initial.adj = fromV;
        initial.parent = EdgeEntry.EMPTY;
        initial.weight = 0;
        bestWays.put(genKey(from), initial);

        EdgeEntry current = initial;
        boolean finished = false;
        while (!finished) {

            System.out.println("NEXT ITER:" + current);
            for (Edge edge : current.adj.getAccepted()) {
                System.out.println(edge);
                EdgeEntry currentEntry = bestWays.get(genKey(edge.next));
                if (currentEntry == null) {
                    EdgeEntry entry = new EdgeEntry();
                    entry.weight = current.weight + 1;
                    entry.adj = key2Vertex.get(genKey(edge.next));
                    entry.parent = current;
                    bestWays.put(genKey(entry.adj), entry);
                    priorityQueue.add(entry);
                } else if (currentEntry.weight > current.weight + 1) {
                    priorityQueue.remove(currentEntry);
                    currentEntry.weight = current.weight + 1;
                    currentEntry.adj = key2Vertex.get(genKey(edge.next));
                    currentEntry.parent = current;
                    priorityQueue.add(currentEntry);
                }
            }

            if (gate.isGoal(current.adj)) {
                current.printPath();
                return true;
            }

            current = priorityQueue.poll();
            if (current == null) {
                System.out.println("SASAI");
                return false;
            }
        }
        return false;
    }

    private long genKey(Point p) {
        return genKey(p.x, p.y);
    }

    private long genKey(int x, int y) {
        return (long) x << 32 | y & 0xFFFFFFFFL;
    }

}
