package edu.geekhub.orcostat.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissileTest {
    @Test
    void default_missile_price_is_4_000_000() {
        final Missile drone = new Missile();

        assertEquals(4_000_000, drone.getPrice());
    }

    @Test
    void missile_price_can_differ_to_default() {
        final Missile drone = new Missile(5_000_000);

        assertEquals(5_000_000, drone.getPrice());
    }
}
