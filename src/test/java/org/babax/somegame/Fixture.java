package org.babax.somegame;

import org.babax.somegame.models.Field;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by blvp on 21.11.15.
 */
public class Fixture {

    public static Field dummyField(){
        return getField(30, 60);
    }

    public static Field mini5x10Field() {
        return getField(5, 10);
    }

    public static Field mini5x4Field() {
        return getField(5, 4);
    }


    public static Field getField(int width, int length) {
        Field field = new Field();
        field.init(width, length);
        field.traps = Collections.emptySet();
        return field;
    }

    public static Engine dummyEngine() {
        Engine engine = new Engine();
        engine.init(Arrays.asList(
                "L1",
                "30", "60",
                "1", "1",
                "0",
                "0", "0",
                "0", "0",
                "0", "0",
                "0", "0"
        ));
        return engine;
    }
}
