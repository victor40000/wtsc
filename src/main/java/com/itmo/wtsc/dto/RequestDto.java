package com.itmo.wtsc.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.itmo.wtsc.utils.ErrorMessages.*;

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
    @Range(groups = {NewRequestCase.class, UpdateRequestCase.class}, min = 1, max = 999, message = INVALID_SIZE_ERROR)
    private Integer size;

    private LocalDateTime createdWhen;

    @Size(groups = {NewRequestCase.class, UpdateRequestCase.class}, max = 200, message = TOO_LONG_DESCRIPTION_ERROR)
    private String description;
}
