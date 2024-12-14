package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum OroVariant {

    DOTTED(0),
    DOTLESS(1),
    TANGE(2),
    INF(3),
    TANGE_2(4);

    private static final OroVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(OroVariant::getId)).toArray(OroVariant[]::new);
    private final int id;

    OroVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static OroVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


