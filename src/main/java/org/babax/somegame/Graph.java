package org.babax.somegame;

import org.babax.somegame.models.*;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Graph {

    public Map<Long, Vertex> key2Vertex;

    public Set<Long> visited;

    public Field field;

    private static int MAX_ITER = 30000;
    private boolean enabled = false;

    public Graph(Field field) {
        this.field = field;
        initNodes();
    }

    public void enable() {
        this.enabled = true;
    }

    private void initNodes() {
        int capacity = field.width * field.length;
        key2Vertex = new HashMap<>(capacity);
        visited = new HashSet<>(capacity);

        for (int y = 0; y <= field.length; y++) {
            for (int x = 0; x <= field.width; x++) {
                Vertex v = initVertex(x, y);
                if (v != Vertex.NONE)
                    key2Vertex.put(genKey(x, y), v);
            }
        }
    }

    private Vertex initVertex(int x, int y) {
        Vertex v = new Vertex();
        v.x = x;
        v.y = y;

        if (field.isTrap(v))
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
        )
                .filter(point -> field.isInField(point)
                        // не ходим по границам
                        && !((x == point.x && field.isBorder(v) && field.isBorder(point))
                        || (y == point.y && field.isBorder(v) && field.isBorder(point)))
                        // середина поля
                        && !(y == field.length / 2 && point.y == y)
                        && !field.isTrap(point))
                .map(point -> {
                    Edge edge = new Edge(v, point);
                    if (!field.isBorder(v) && field.isBorder(point))
                        edge.weight = 0;
                    return edge;
                })
                .collect(toSet());
        return v;
    }

    public void markVisited(Point p) {
        long key = genKey(p);
        visited.add(key);
    }

    public boolean trackMove(Point adj, Point next) {
        markVisited(next);
        return markDisabled(adj, next) && markDisabled(next, adj);
    }

    private boolean markDisabled(Point adj, Point next) {
        Vertex v = key2Vertex.get(genKey(adj));
        if (v == null)
            throw new IllegalStateException();

        for (Edge edge : v.edges) {
            if (edge.disabled)
                continue;
            Point eNext = edge.next;
            if (eNext.x == next.x && eNext.y == next.y) {
                edge.disabled = true;
                return true;
            }
        }
        return false;

    }

    private Set<Long> prepareKeepers(List<Point> keepers) {
        Set<Long> keys = new HashSet<>(4);
        for(Point keeper : keepers) {
            keys.add(genKey(keeper));
        }
        return keys;
    }

    public EdgeEntry findMove(Point from, Gate gate, List<Point> keepers) {
        Set<Long> keepersKeys = prepareKeepers(keepers);

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
        int counter = 0;

        EdgeEntry maybeBest = initial;
        int minimalLenght = Integer.MAX_VALUE;

        while (!finished) {
            counter++;

            int tmpMinLength = lengthDistance(gate, current.adj);
            if (tmpMinLength < minimalLenght) {
                minimalLenght = tmpMinLength;
                maybeBest = current;
            }

            for (Edge edge : current.adj.edges) {
                if (edge.disabled)
                    continue;

                long key = genKey(edge.next);
                if (keepersKeys.contains(key))
                    continue;

                EdgeEntry currentEntry = bestWays.get(key);
                int tmpWeight = edge.weight;
                if(visited.contains(genKey(edge.next)))
                    tmpWeight = 0;

                int edgeWeightNew = current.weight + tmpWeight;

                if (currentEntry == null) {
                    EdgeEntry entry = new EdgeEntry();
                    entry.weight = edgeWeightNew;
                    entry.adj = key2Vertex.get(key);
                    entry.parent = current;
                    bestWays.put(genKey(entry.adj), entry);
                    priorityQueue.add(entry);
                } else if (currentEntry.weight > edgeWeightNew) {
                    priorityQueue.remove(currentEntry);
                    currentEntry.weight = edgeWeightNew;
                    currentEntry.adj = key2Vertex.get(key);
                    currentEntry.parent = current;
                    priorityQueue.add(currentEntry);
                } else if (currentEntry.weight == edgeWeightNew) {
                    priorityQueue.remove(currentEntry);
                    currentEntry.weight = edgeWeightNew;
                    currentEntry.adj = key2Vertex.get(key);
                    currentEntry.parent = current;
                    priorityQueue.add(currentEntry);
                }
            }

            if (gate.isGoal(current.adj)) {
                return current;
            }

            if (enabled && counter >= MAX_ITER) {
                return maybeBest;
            }

            current = priorityQueue.poll();
            if (current == null) {
                System.out.println("SASAI");
                return EdgeEntry.EMPTY;
            }
        }

        return EdgeEntry.EMPTY;
    }

    private int lengthDistance(Gate gate, Vertex v) {
        if (gate.top.y == 0)
            return v.y;
        return gate.top.y - v.y;
    }

    private long genKey(Point p) {
        return genKey(p.x, p.y);
    }

    private long genKey(int x, int y) {
        return (long) x << 32 | y & 0xFFFFFFFFL;
    }

    protected Vertex getVertex(int x, int y) {
        return key2Vertex.get(genKey(x, y));
    }

}
