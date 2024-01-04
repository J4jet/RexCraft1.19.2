package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BorealVariant {

    M(0),
    F(1);

    private static final BorealVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BorealVariant::getId)).toArray(BorealVariant[]::new);
    private final int id;

    BorealVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BorealVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


