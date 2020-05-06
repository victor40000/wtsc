package com.itmo.wtsc.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.itmo.wtsc.utils.ErrorMessages.FIELD_NULL_ERROR;

@Data
public class PointDto {

    @NotNull(groups = {NewRequestCase.class}, message = FIELD_NULL_ERROR)
    private Double longitude;

    @NotNull(groups = {NewRequestCase.class}, message = FIELD_NULL_ERROR)
    private Double latitude;
}
