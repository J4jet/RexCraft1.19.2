package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum SinoVariant {

    M1(0),
    F1(1);

    private static final SinoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(SinoVariant::getId)).toArray(SinoVariant[]::new);
    private final int id;

    SinoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SinoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


