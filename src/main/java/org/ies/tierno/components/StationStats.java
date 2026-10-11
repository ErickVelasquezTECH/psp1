package org.ies.tierno.components;

import org.ies.tierno.model.StatsSnapshot;

public class StationStats {
    private int totalKwh;
    private long totalCents;


    public synchronized void register(int kwh) {
        totalKwh += kwh;
        totalCents += (long) kwh * 45;
    }

    public synchronized StatsSnapshot snapshot() {
        return new StatsSnapshot(totalKwh, totalCents);
    }
}

