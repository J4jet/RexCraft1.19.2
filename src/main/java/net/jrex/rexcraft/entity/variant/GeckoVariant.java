package net.jrex.rexcraft.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GeckoVariant {

    DOTTED(0),
    DOTLESS(1);

    private static final GeckoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GeckoVariant::getId)).toArray(GeckoVariant[]::new);
    private final int id;

    GeckoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GeckoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}


