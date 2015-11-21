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
        while(curr != null) {
            System.out.println("Path:");
            System.out.println("     " + curr.adj);
            curr = curr.parent;
        }
    }
}
