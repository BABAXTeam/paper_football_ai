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
                if(v != Vertex.NONE)
                    key2Vertex.put(genKey(x, y), v);
            }
        }
    }

    private Vertex initVertex(int x, int y) {
        Vertex v = new Vertex();
        v.x = x;
        v.y = y;

        if(field.isTrap(v))
            return Vertex.NONE;

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
                // не ходим по границам
                && !((x == point.x && field.isBorder(v) && field.isBorder(point))
                      || (y == point.y && field.isBorder(v) && field.isBorder(point)))
                // середина поля
                && !(y == field.length/2 && point.y == y)
                && !field.isTrap(point))
                .map(point -> new Edge(v, point))
                .collect(toSet());
        return v;
    }

    public boolean markDisabled(Point adj, Point next) {
        Vertex v = key2Vertex.get(genKey(adj));
        if(v == null)
            throw new IllegalStateException();

        for(Edge edge : v.getAccepted()) {
            Point eNext = edge.next;
            if(eNext.x == next.x && eNext.y == next.y) {
                edge.disabled = true;
                return true;
            }
        }
        return false;
    }

    public EdgeEntry findMove(Point from, Gate gate) {
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

            for (Edge edge : current.adj.getAccepted()) {
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
                //current.printPath();
                return current;
            }

            current = priorityQueue.poll();
            if (current == null) {
                System.out.println("SASAI");
                return EdgeEntry.EMPTY;
            }
        }

        return EdgeEntry.EMPTY;
    }

    private long genKey(Point p) {
        return genKey(p.x, p.y);
    }

    private long genKey(int x, int y) {
        return (long) x << 32 | y & 0xFFFFFFFFL;
    }

}
