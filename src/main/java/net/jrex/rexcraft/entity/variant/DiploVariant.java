package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum DiploVariant {

    M(0),
    F(1);

    private static final DiploVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(DiploVariant::getId)).toArray(DiploVariant[]::new);
    private final int id;

    DiploVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DiploVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


