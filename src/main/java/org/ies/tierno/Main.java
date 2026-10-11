package org.ies.tierno;

import org.ies.tierno.components.ChargingStation;
import org.ies.tierno.components.StationStats;
import org.ies.tierno.model.StatsSnapshot;
import org.ies.tierno.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        StationStats stats = new StationStats();
        ChargingStation station = new ChargingStation(stats);
        Random random = new Random();


        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String plate = random.nextInt(10000) + "E";
            int kwh = random.nextInt(1, 101);
            vehicles.add(new Vehicle(plate, kwh));
        }

        try (ExecutorService pool = Executors.newFixedThreadPool(8)) {
            for (Vehicle vehicle : vehicles) {
                pool.submit(() -> {
                    station.charge(vehicle);
                });
            }
        }

        StatsSnapshot snapshot = stats.snapshot();

        System.out.println("Resumen final:");
        System.out.println("- Total kWh: " + snapshot.totalKwh());
        System.out.println("- Total centimos: " + snapshot.totalCents());

        int sumKwh = 0;

        for (Vehicle vehicle : vehicles) {
            sumKwh += vehicle.kwh();
        }

        System.out.println("¿Coinciden los kWh? " + ((sumKwh == snapshot.totalKwh())?"Si":"No"));

        System.out.println("¿Coinciden los centimos? " + ((snapshot.totalCents() == snapshot.totalKwh() * 45L)?"Si":"No"));
    }
}