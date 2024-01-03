package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BernisVariant {
    GREEN_M(1),
    GREEN_F(2);

    private static final BernisVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BernisVariant::getId)).toArray(BernisVariant[]::new);
    private final int id;

    BernisVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BernisVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


