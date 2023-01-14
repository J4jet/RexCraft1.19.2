package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum HedgyVariant {
    DARK(0),
    LIGHT(1);


    private static final HedgyVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HedgyVariant::getId)).toArray(HedgyVariant[]::new);
    private final int id;

    HedgyVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HedgyVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
