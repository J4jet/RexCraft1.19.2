package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum JakaVariant {

    M(0),
    F(1);

    private static final JakaVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(JakaVariant::getId)).toArray(JakaVariant[]::new);
    private final int id;

    JakaVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static JakaVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


