package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PrenoVariant {

    M(0),
    F(1);

    private static final PrenoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PrenoVariant::getId)).toArray(PrenoVariant[]::new);
    private final int id;

    PrenoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PrenoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


