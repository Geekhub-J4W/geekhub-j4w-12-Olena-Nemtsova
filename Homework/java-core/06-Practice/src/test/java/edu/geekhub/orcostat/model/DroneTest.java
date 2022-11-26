package edu.geekhub.orcostat.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {
    @Test
    void default_drone_price_is_50_000() {
        final Drone drone = new Drone();

        assertEquals(50_000, drone.getPrice());
    }

    @Test
    void drone_price_can_differ_to_default() {
        final Drone drone = new Drone(40_000);

        assertEquals(40_000, drone.getPrice());
    }
}
