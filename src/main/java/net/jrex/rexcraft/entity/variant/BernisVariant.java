package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BernisVariant {

    BLUE_M(0),
    BLUE_F(1),
    GREEN_M(2),
    GREEN_F(4);

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


