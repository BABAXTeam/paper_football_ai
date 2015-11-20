package org.babax.somegame;

import org.babax.somegame.models.Edge;
import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;
import org.babax.somegame.models.Vertex;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Graph {

    private List<Vertex> nodes;
    private Field field;

    public Graph(Field field) {
        this.field = field;
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.width; x++) {
                Vertex v = initVertex(y, x);
                nodes.add(v);
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
                )
                .map(point -> new Edge(v, point))
                .collect(toSet());
        return v;
    }
}
