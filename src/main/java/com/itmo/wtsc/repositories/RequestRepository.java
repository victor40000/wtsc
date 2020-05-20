package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Integer> {

    List<Request> findRequestByStatusIn(List<RequestStatus> statuses);

    List<Request> findRequestsByStatusInAndDumpTypeInAndSizeLessThanEqualAndCreatedWhenBetween(
            List<RequestStatus> statuses,
            List<DumpType> types,
            Integer maxSize,
            LocalDateTime startTime,
            LocalDateTime endTime);

    List<Request> findRequestsByCreatedWhenIsAfterAndCreatedWhenIsBefore(
            LocalDateTime startTime,
            LocalDateTime endTime);

    List<Request> findRequestsByCreatedWhenIsBetween(
            LocalDateTime startTime,
            LocalDateTime endTime);
}
