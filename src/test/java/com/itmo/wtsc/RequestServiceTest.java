package com.itmo.wtsc;

import com.itmo.wtsc.repositories.PointRepository;
import com.itmo.wtsc.repositories.RequestRepository;
import com.itmo.wtsc.repositories.UserRepository;
import com.itmo.wtsc.services.GeoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestServiceTest {

    private static TestConfig testConfig;

    @BeforeAll
    public static void initializeConfig() {
        testConfig = new TestConfig();
    }

    @Test
    public void testAddRequest() {
        GeoService geoService = testConfig.getGeoService();
        UserRepository userRepository = testConfig.getUserRepository();
        RequestRepository requestRepository = testConfig.getRequestRepository();
        PointRepository pointRepository = testConfig.getPointRepository();
        Double latitude, longitude;
        latitude = 60.2;
        longitude = 30.1;
        when(geoService.isLand(latitude, longitude)).thenReturn(true);
        when(geoService.isNationalPark(latitude, longitude)).thenReturn(true);
    }

}
