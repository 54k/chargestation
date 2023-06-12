package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.VehicleDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDocRepository extends ElasticsearchRepository<VehicleDoc, String> {
    List<VehicleDoc> findVehicleSearchItemsByRegistrationPlateContainingIgnoreCaseAndNameContainingIgnoreCase(
        String registrationPlate, String name);
}
