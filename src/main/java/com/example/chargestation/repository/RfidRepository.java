package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.Rfid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RfidRepository extends CrudRepository<Rfid, String>, PagingAndSortingRepository<Rfid, String> {
}
