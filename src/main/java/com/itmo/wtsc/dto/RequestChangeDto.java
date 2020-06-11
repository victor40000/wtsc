package com.itmo.wtsc.dto;

import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestChangeDto {

    private Integer id;

    private RequestStatus from;

    private RequestStatus to;

    private LocalDateTime updatedWhen;

    private RequestDto request;
}
