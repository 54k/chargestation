package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends CrudRepository<Point, String> {
}
