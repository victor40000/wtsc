package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.Request;
import com.itmo.wtsc.utils.enums.RequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Integer> {

    List<Request> findRequestByStatusIn(List<RequestStatus> statuses);
}
