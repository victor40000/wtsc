package com.itmo.wtsc.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.itmo.wtsc.utils.ErrorMessages.FIELD_NULL_ERROR;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({"id"})
public class RequestDto extends PointDto {

    private Integer id;

    @NotNull(groups = {NewRequestCase.class, UpdateRequestCase.class}, message = FIELD_NULL_ERROR)
    private DumpType dumpType;

    @NotNull(groups = {UpdateRequestCase.class}, message = FIELD_NULL_ERROR)
    private RequestStatus status;

    @NotNull(groups = {NewRequestCase.class, UpdateRequestCase.class}, message = FIELD_NULL_ERROR)
    private Integer size;

    private LocalDateTime createdWhen;
    
    private String description;
}
