package org.babax.somegame.models;

import java.util.Set;

public class Field {

    public int width;
    public int length;
    public Set<Point> traps;

    public Gate gate1;
    public Gate gate2;

    public void init(int width, int length) {
        this.width = width;
        this.length = length;
        matchGates(width, length);
    }

    private void matchGates(int width, int length) {
        int k = width / 5;
        gate1 = new Gate();
        gate1.top = new Point(2 * k, 0);
        gate1.bottom = new Point(3 * k, 0);

        gate2 = new Gate();
        gate2.top = new Point(2 * k, length);
        gate2.bottom = new Point(3 * k, length);
    }

    public boolean isTrap(Point p) {
        return traps.contains(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (width != field.width) return false;
        if (length != field.length) return false;
        if (traps != null ? !traps.equals(field.traps) : field.traps != null) return false;
        if (gate1 != null ? !gate1.equals(field.gate1) : field.gate1 != null) return false;
        return !(gate2 != null ? !gate2.equals(field.gate2) : field.gate2 != null);

    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + length;
        result = 31 * result + (traps != null ? traps.hashCode() : 0);
        result = 31 * result + (gate1 != null ? gate1.hashCode() : 0);
        result = 31 * result + (gate2 != null ? gate2.hashCode() : 0);
        return result;
    }
}
