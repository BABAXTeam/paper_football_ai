package org.babax.somegame.models;

public class EdgeEntry implements Comparable<EdgeEntry> {

    public static EdgeEntry EMPTY = new EdgeEntry();

    public EdgeEntry parent;
    public Vertex adj;
    public int weight;

    @Override
    public int compareTo(EdgeEntry o) {
        return weight - o.weight;
    }

    @Override
    public String toString() {
        return "EdgeEntry{" +
                "adj=" + adj +
                ", weight=" + weight +
                '}';
    }

    public void printPath() {
        EdgeEntry curr = this;
        System.out.println("Path:");
        while(curr != null) {
            System.out.println("     " + curr.adj);
            curr = curr.parent;
        }
    }

    public Point getFirst() {
        EdgeEntry curr = this;
        while(curr.parent != EdgeEntry.EMPTY && curr.parent.parent != EdgeEntry.EMPTY) {
            curr = curr.parent;
        }
        return curr.adj;
    }
}
