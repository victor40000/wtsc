package com.itmo.wtsc.dto;

import com.itmo.wtsc.entities.RequestChange;
import lombok.Data;

import java.util.List;

@Data
public class RequestChangesStatisticDto {

    private List<RequestChangeDto> requestChanges;
}
