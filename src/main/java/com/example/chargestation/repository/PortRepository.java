package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.Port;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortRepository extends CrudRepository<Port, String>, PagingAndSortingRepository<Port, String> {
}
