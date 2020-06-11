package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.RequestChange;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestChangeRepository extends CrudRepository<RequestChange, Integer> {

    List<RequestChange> findAllByUpdatedWhenBetween(LocalDateTime start, LocalDateTime end);
}
