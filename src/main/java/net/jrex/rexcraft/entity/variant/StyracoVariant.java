package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum StyracoVariant {

    M1(0),
    F1(1),
    M2(2),
    F2(3);

    private static final StyracoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(StyracoVariant::getId)).toArray(StyracoVariant[]::new);
    private final int id;

    StyracoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static StyracoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


