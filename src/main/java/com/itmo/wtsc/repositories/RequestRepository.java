package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends CrudRepository<Request, Integer> {

    List<Request> findRequestByStatusInAndArchivedIsFalse(List<RequestStatus> statuses);

    Optional<Request> findByIdAndArchivedIsFalse(Integer id);

    List<Request> findAllByArchivedIsFalse();

    List<Request> findRequestsByStatusInAndDumpTypeInAndSizeLessThanEqualAndCreatedWhenBetweenAndArchivedIsFalse(
            List<RequestStatus> statuses,
            List<DumpType> types,
            Integer maxSize,
            LocalDateTime startTime,
            LocalDateTime endTime);

}
