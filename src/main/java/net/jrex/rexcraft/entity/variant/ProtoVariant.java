package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ProtoVariant {

    M1(0),
    F1(1),
    M2(2),
    F2(3);

    private static final ProtoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ProtoVariant::getId)).toArray(ProtoVariant[]::new);
    private final int id;

    ProtoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ProtoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


