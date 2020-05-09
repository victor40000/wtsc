package com.itmo.wtsc.services;

public interface GeoService {

    boolean isLand(Double latitude, Double longitude);

    boolean isNationalPark(Double latitude, Double longitude);
}
