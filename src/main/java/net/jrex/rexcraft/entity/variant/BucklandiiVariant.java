package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BucklandiiVariant {

    BROWN_M(0),
    BROWN_F(1);

    private static final BucklandiiVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BucklandiiVariant::getId)).toArray(BucklandiiVariant[]::new);
    private final int id;

    BucklandiiVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BucklandiiVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


