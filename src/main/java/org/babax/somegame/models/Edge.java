package org.babax.somegame.models;

public class Edge {
    public Point adj;
    public Point next;
    public int weight = 1;

    public boolean disabled;

    public Edge(Point adj, Point next) {
        this.adj = adj;
        this.next = next;
    }
}
