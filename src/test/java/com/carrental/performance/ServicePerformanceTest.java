package com.carrental.performance;

import com.carrental.service.*;
import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ServicePerformanceTest {

    @Autowired private BookingService bookingService;
    @Autowired private CarService carService;
    @Autowired private InsuranceService insuranceService;
    @Autowired private UserService userService;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG =
        JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/ServiceTestsReport.html"))
            .build();

    // --- BookingService ---

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 500, percentiles = "95:30ms,99:80ms", allowedErrorPercentage = 0.0f)
    public void testGetAllBookings() {
        bookingService.getAllBookings();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 400, percentiles = "95:40ms,99:100ms", allowedErrorPercentage = 0.0f)
    public void testGetPendingBookings() {
        bookingService.getPendingBookings();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 400, percentiles = "95:50ms,99:100ms", allowedErrorPercentage = 0.0f)
    public void testGetHistoryBookings() {
        bookingService.getHistoryBookings();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 500, percentiles = "95:20ms,99:50ms", allowedErrorPercentage = 0.0f)
    public void testCountActiveBookings() {
        bookingService.countActiveBookings();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 500, percentiles = "95:25ms,99:60ms", allowedErrorPercentage = 0.0f)
    public void testGetTotalRevenue() {
        bookingService.getTotalRevenue();
    }

    // --- CarService ---

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 800, percentiles = "95:20ms,99:30ms", allowedErrorPercentage = 0.0f)
    public void testGetAllCars() {
        carService.getAllCars();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 600, percentiles = "95:25ms,99:40ms", allowedErrorPercentage = 0.0f)
    public void testCountAvailableCars() {
        carService.countAvailableCars();
    }

    // --- InsuranceService ---

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 1000, percentiles = "95:15ms,99:30ms", allowedErrorPercentage = 0.0f)
    public void testGetAllInsurances() {
        insuranceService.getAllInsurances();
    }

    // --- UserService ---

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 1000, percentiles = "95:15ms,99:30ms", allowedErrorPercentage = 0.0f)
    public void testGetAllUsers() {
        userService.getAllUsers();
    }
}
