package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.Point;
import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<Point, Integer> {
}
