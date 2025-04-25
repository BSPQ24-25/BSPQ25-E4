package com.carrental.performance;

import com.github.noconnor.junitperf.JUnitPerfTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingPerformanceTest {

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testBookingPerformance() {
        long startTime = System.currentTimeMillis();

        // Simula lógica que quieres probar
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 2000, "El test de rendimiento no pasó el límite de tiempo");
    }
}
