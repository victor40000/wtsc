package com.itmo.wtsc.osm;

import com.itmo.wtsc.services.GeoService;
import com.itmo.wtsc.utils.ErrorMessages;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.itmo.wtsc.utils.ErrorMessages.COORDINATES_NOT_IN_NATIONAL_PARK_ERROR;
import static com.itmo.wtsc.utils.ErrorMessages.COORDINATES_ON_WATER_ERROR;

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
    public void validateCoordinates(Double latitude, Double longitude) {
        String areas = restClient.getAreas(latitude, longitude);
        if (!areas.contains(NATIONAL_PARK_KEY)) {
            throw new ValidationException(String.format(COORDINATES_NOT_IN_NATIONAL_PARK_ERROR,
                    latitude, longitude));
        }
        if (areas.contains(LAKE_KEY)) {
            throw new ValidationException(String.format(COORDINATES_ON_WATER_ERROR, latitude, longitude));
        }
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

    @Override
    public boolean isRadiusIntersecting(Double lat1, Double lon1, Double lat2, Double lon2) {
        //haversine formula
        final int R = 6371;
        double latDistance = toRad(lat2 - lat1);
        double lonDistance = toRad(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        return distance < 20;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}
