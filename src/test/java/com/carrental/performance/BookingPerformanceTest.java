package com.carrental.performance;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;


@ExtendWith(JUnitPerfInterceptor.class)
public class BookingPerformanceTest {

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 5)
    public void testBookingPerformance() {
        // Simulación de lógica de reserva
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
