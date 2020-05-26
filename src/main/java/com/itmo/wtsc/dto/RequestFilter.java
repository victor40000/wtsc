package com.itmo.wtsc.dto;

import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@Getter
@Setter
public class RequestFilter {

    private static final Integer MAX_SIZE = 9999;

    private List<RequestStatus> statuses;
    private List<DumpType> types;
    private Integer maxSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RequestFilter(List<RequestStatus> statuses, List<DumpType> types, Integer maxSize, LocalDateTime startTime, LocalDateTime endTime) {
        this.statuses = statuses;
        this.types = types;
        this.maxSize = maxSize;
        this.startTime = startTime;
        this.endTime = endTime;
        if (statuses == null) {
            this.statuses = Arrays.asList(RequestStatus.WAITING, RequestStatus.IN_PROGRESS,
                    RequestStatus.COMPLETED, RequestStatus.CANCELLED);
        }
        if (types == null) {
            this.types = Arrays.asList(DumpType.MIXED, DumpType.SOLID,
                    DumpType.LIQUID, DumpType.HOUSEHOLD);
        }
        if (maxSize == null) {
            this.maxSize = MAX_SIZE;
        }
        if (startTime == null) {
            this.startTime = LocalDateTime.parse("2000-05-20T15:23:30");
        }
        if (endTime == null) {
            this.endTime = LocalDateTime.parse("2100-05-20T15:23:30");
        }
    }
}
