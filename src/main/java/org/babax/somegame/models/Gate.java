package org.babax.somegame.models;

public class Gate {
    public Point top, bottom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gate gate = (Gate) o;

        if (top != null ? !top.equals(gate.top) : gate.top != null) return false;
        return !(bottom != null ? !bottom.equals(gate.bottom) : gate.bottom != null);

    }

    @Override
    public int hashCode() {
        int result = top != null ? top.hashCode() : 0;
        result = 31 * result + (bottom != null ? bottom.hashCode() : 0);
        return result;
    }
}
