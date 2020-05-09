package com.itmo.wtsc;

import com.itmo.wtsc.osm.OsmRestClient;
import com.itmo.wtsc.osm.OsmService;
import com.itmo.wtsc.repositories.PointRepository;
import com.itmo.wtsc.repositories.RequestRepository;
import com.itmo.wtsc.repositories.UserRepository;
import com.itmo.wtsc.services.GeoService;
import com.itmo.wtsc.services.RequestService;
import com.itmo.wtsc.services.UserService;
import lombok.Getter;
import org.mockito.Mockito;

@Getter
public class TestConfig {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PointRepository pointRepository = Mockito.mock(PointRepository.class);
    private final RequestRepository requestRepository = Mockito.mock(RequestRepository.class);
    private final GeoService geoService = Mockito.mock(GeoService.class);

    private final UserService userService = new UserService(userRepository);
    private final RequestService requestService = new RequestService(requestRepository, pointRepository,
            userService, geoService);
}
