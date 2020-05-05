package com.itmo.wtsc.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itmo.wtsc.ErrorMessages;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

import static com.itmo.wtsc.ErrorMessages.FIELD_NULL_ERROR;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({"id"})
public class RequestDto extends PointDto {

    private Integer id;

    @NotNull(groups = {NewCase.class, UpdateRequestCase.class}, message = FIELD_NULL_ERROR)
    private DumpType dumpType;

    @NotNull(groups = {UpdateStatusCase.class}, message = FIELD_NULL_ERROR)
    private RequestStatus status;

    @NotNull(groups = {NewCase.class, UpdateRequestCase.class}, message = FIELD_NULL_ERROR)
    private Integer size;

    private String description;
}
