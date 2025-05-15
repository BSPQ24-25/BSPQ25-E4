package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import com.carrental.repository.InsuranceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminDashboardControllerTest {
	private static final Logger logger = LogManager.getLogger(AdminDashboardControllerTest.class);
    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @Mock
    private BookingService bookingService;

    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private AdminDashboardController controller;

    @BeforeEach
    public void setup() {
    	logger.info("Setting up AdminDashboardControllerTest");
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        logger.info("MockMvc setup complete");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDashboardStats() throws Exception {
    	logger.info("Running testDashboardStats");
        when(carService.countAvailableCars()).thenReturn(5L);
        when(bookingService.countActiveBookings()).thenReturn(3L);
        when(bookingService.getTotalRevenue()).thenReturn(1234.56);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_dashboard"))
                .andExpect(model().attribute("availableCars", 5L))
                .andExpect(model().attribute("activeRentals", 3L))
                .andExpect(model().attribute("totalRevenue", 1234.56));
        logger.info("Dashboard stats test completed successfully");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testShowVehicleList() throws Exception {
		logger.info("Running testShowVehicleList");
		
        when(carService.getAllCars()).thenReturn(Arrays.asList(new Car(), new Car()));

        mockMvc.perform(get("/admin/vehicles"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/vehicle-management"))
                .andExpect(model().attributeExists("vehicles"));
        logger.info("Vehicle list test completed successfully");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddVehicleWithInsurance() throws Exception {
    	logger.info("Running testAddVehicleWithInsurance");
        Long insuranceId = 1L;
        Insurance insurance = new Insurance();
        Car car = new Car();

        when(insuranceRepository.findById(insuranceId)).thenReturn(Optional.of(insurance));

        mockMvc.perform(post("/admin/vehicles/add")
                        .param("insuranceId", insuranceId.toString())
                        .flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vehicles"));

        verify(carService).saveCar(car);
        logger.info("Vehicle added with insurance successfully");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testShowEditForm() throws Exception {
    	logger.info("Running testShowEditForm");
        Car car = new Car();
        when(carService.getCarById(1L)).thenReturn(Optional.of(car));
        when(insuranceRepository.findAll()).thenReturn(Arrays.asList(new Insurance()));

        mockMvc.perform(get("/admin/vehicles/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit-vehicle"))
                .andExpect(model().attribute("car", car))
                .andExpect(model().attributeExists("insurances"));
        logger.info("Edit form test completed successfully");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testShowEditFormCarNotFound() throws Exception {
		logger.info("Running testShowEditFormCarNotFound");
		
        when(carService.getCarById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/vehicles/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vehicles?error=car-not-found"));
        logger.info("Edit form for non-existing car redirected successfully");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateVehicleWithInsurance() throws Exception {
    	logger.info("Running testUpdateVehicleWithInsurance");
        Car car = new Car();
        Insurance insurance = new Insurance();
        Long insuranceId = 2L;

        when(insuranceRepository.findById(insuranceId)).thenReturn(Optional.of(insurance));

        mockMvc.perform(post("/admin/vehicles/edit")
                        .param("insuranceId", insuranceId.toString())
                        .flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vehicles"));

        verify(carService).saveCar(car);
        logger.info("Vehicle updated with insurance successfully");
    }
}