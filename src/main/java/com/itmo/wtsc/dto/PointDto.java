package com.itmo.wtsc.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.itmo.wtsc.ErrorMessages.FIELD_NULL_ERROR;

@Data
public class PointDto {

    @NotNull(groups = {NewCase.class}, message = FIELD_NULL_ERROR)
    private Double longitude;

    @NotNull(groups = {NewCase.class}, message = FIELD_NULL_ERROR)
    private Double latitude;
}
