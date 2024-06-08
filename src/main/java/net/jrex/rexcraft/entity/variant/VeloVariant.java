package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum VeloVariant {

    M1(0),
    F1(1);

    private static final VeloVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(VeloVariant::getId)).toArray(VeloVariant[]::new);
    private final int id;

    VeloVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static VeloVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


