package com.itmo.wtsc.utils;

public interface ErrorMessages {

    String INCORRECT_FIELD_ERROR = "Field: %s is incorrect: %s";
    String FIELD_NULL_ERROR = "Field shouldn't be null";
    String INVALID_SIZE_ERROR = "Size should be in range [1, 999]";
    String TOO_LONG_DESCRIPTION_ERROR = "Description is too long";

    String REQUEST_NOT_FOUND_ERROR = "Request with id: %s not found";
    String INVALID_STATUS_TRANSITION_ERROR = "Request with status: %s couldn't be shifted to status: %s";
    String UNCHANGEABLE_REQUEST_ERROR = "Couldn't change request with status: %s";
    String COULD_NOT_DELETE_REQUEST_ERROR = "Couldn't change request with status: %s";
    String COORDINATES_NOT_IN_NATIONAL_PARK_ERROR = "Point with latitude: %s and longitude: %s is not located in national park";
    String COORDINATES_ON_WATER_ERROR = "Point with latitude: %s and longitude: %s is not located on land";
    String REQUEST_ALREADY_EXISTS = "Request for this dump already exists";
}
