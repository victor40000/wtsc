package com.itmo.wtsc.utils;

public interface ErrorMessages {

    String INCORRECT_FIELD_ERROR = "Field: %s is incorrect: %s";
    String FIELD_NULL_ERROR = "Field shouldn't be null";

    String REQUEST_NOT_FOUND_ERROR = "Request with id: %s not found";
    String INVALID_STATUS_TRANSITION_ERROR = "Request with status: %s couldn't be shifted to status: %s";
    String UNCHANGEABLE_REQUEST_ERROR = "Couldn't change request with status: %s";
    String COORDINATES_NOT_IN_NATIONAL_PARK_ERROR = "Point with latitude: %s and longitude: %s is not located in national park";
    String COORDINATES_ON_WATER_ERROR = "Point with latitude: %s and longitude: %s is not located on land";
}
