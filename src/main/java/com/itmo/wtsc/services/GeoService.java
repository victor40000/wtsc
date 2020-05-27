package com.itmo.wtsc.services;

public interface GeoService {

    void validateCoordinates(Double latitude, Double longitude);

    boolean isLand(Double latitude, Double longitude);

    boolean isNationalPark(Double latitude, Double longitude);

    boolean isRadiusIntersecting(Double lat1, Double lon1, Double lat2, Double lon2);
}
