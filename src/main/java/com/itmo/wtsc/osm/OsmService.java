package com.itmo.wtsc.osm;

import com.itmo.wtsc.services.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OsmService implements GeoService {

    public static final String LAKE_KEY = "Ladoga";
    public static final String NATIONAL_PARK_KEY = "Laatokan saariston kansallispuisto";
    private final OsmRestClient restClient;

    @Autowired
    public OsmService(OsmRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean isLand(Double latitude, Double longitude) {
        String areas = restClient.getAreas(latitude, longitude);
        return !areas.contains(LAKE_KEY);
    }

    @Override
    public boolean isNationalPark(Double latitude, Double longitude) {
        String areas = restClient.getAreas(latitude, longitude);
        return areas.contains(NATIONAL_PARK_KEY);
    }
}
