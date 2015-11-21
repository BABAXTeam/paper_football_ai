package org.babax.somegame;

import org.babax.somegame.models.Vertex;

/**
 * Created by dos65 on 21.11.15.
 */
public class Util {

    public static int getInt(String str) {
        return Integer.valueOf(str);
    }


    public static int getEdgesCount(Graph g) {
        int count = 0;
        for(Vertex v : g.key2Vertex.values()) {
            count += v.getAccepted().size();
        }
        return count;
    }
}
