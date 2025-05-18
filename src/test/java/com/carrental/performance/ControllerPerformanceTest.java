package com.carrental.performance;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(JUnitPerfInterceptor.class)
public class ControllerPerformanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator("target/reports/ControllerPerformanceTestReport.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:40ms,99:100ms", allowedErrorPercentage = 0.0f)
    public void testGetAllBookings() {
        restTemplate.getForEntity("/api/bookings", String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:30ms,99:50ms", allowedErrorPercentage = 0.0f)
    public void testGetAllCars() {
        restTemplate.getForEntity("/cars", String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:35ms,99:80ms", allowedErrorPercentage = 0.0f)
    public void testGetAllInsurances() {
        restTemplate.getForEntity("/admin/insurances", String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:30ms,99:60ms", allowedErrorPercentage = 0.0f)
    public void testUserProfile() {
        restTemplate.getForEntity("/user/profile", String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:40ms,99:90ms", allowedErrorPercentage = 0.0f)
    public void testUserDashboard() {
        restTemplate.getForEntity("/user/dashboard", String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 100, percentiles = "95:35ms,99:80ms", allowedErrorPercentage = 0.0f)
    public void testUserRentalHistory() {
        restTemplate.getForEntity("/history", String.class);
    }
}
