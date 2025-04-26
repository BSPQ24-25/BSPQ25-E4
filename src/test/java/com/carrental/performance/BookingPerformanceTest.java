package com.carrental.performance;

import com.carrental.service.BookingService;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class BookingPerformanceTest {

    @Autowired
    private BookingService bookingService;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
        .build();

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 1, allowedErrorPercentage = 100)
    public void testGetAllBookingsPerformance() {
        bookingService.getAllBookings();
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 1, allowedErrorPercentage = 100)
    public void testGetPendingBookingsPerformance() {
        bookingService.getPendingBookings();
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 1, allowedErrorPercentage = 100)
    public void testGetHistoryBookingsPerformance() {
        bookingService.getHistoryBookings();
    }
}
