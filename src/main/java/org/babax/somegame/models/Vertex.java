package org.babax.somegame.models;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Vertex extends Point {

    public static Vertex NONE = new Vertex();

    public Set<Edge> edges;

    public Set<Edge> getAccepted() {
        return edges.stream().filter(edge -> !edge.disabled).collect(toSet());
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
}
