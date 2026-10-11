package org.ies.tierno.components;

import org.ies.tierno.model.ChargeResult;
import org.ies.tierno.model.Vehicle;

import java.util.concurrent.Semaphore;

public class ChargingStation {

    private final StationStats stationStats;
    private final Semaphore semaphore = new Semaphore(4);

    public ChargingStation(StationStats stationStats) {
        this.stationStats = stationStats;
    }

    public ChargeResult charge(Vehicle vehicle) {

        try {
            semaphore.acquire();

            System.out.println("Vehiculo " + vehicle.plate() + " empieza a cargar");

            Thread.sleep(vehicle.kwh() * 20L);

            stationStats.register(vehicle.kwh());

            System.out.println("Vehiculo " + vehicle.plate() + " termina y libera el cargador");

            return new ChargeResult(
                    vehicle.plate(),
                    vehicle.kwh(),
                    (long) vehicle.kwh() * 45
            );

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
}