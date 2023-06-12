package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findCustomerByName(String name);
}
