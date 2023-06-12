package com.example.chargestation.repository;

import com.example.chargestation.repository.schema.Port;
import com.example.chargestation.repository.schema.Rfid;
import com.example.chargestation.repository.schema.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SessionRepository
    extends CrudRepository<Session, String>, PagingAndSortingRepository<Session, String>
{
    long countSessionsByRfidAndPortAndEndDateIsNull(Rfid rfid, Port port);

    Session findSessionByPortAndEndDateIsNull(Port port);

    List<Session> findSessionsByStartDateBetween(Date from, Date to);
}
