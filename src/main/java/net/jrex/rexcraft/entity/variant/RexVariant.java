package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum RexVariant {

    M1(0),
    F1(1);

    private static final RexVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(RexVariant::getId)).toArray(RexVariant[]::new);
    private final int id;

    RexVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RexVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


