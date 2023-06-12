package com.example.chargestation.repository.schema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "session")
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Date startDate;
    Date endDate;
    String startValue;
    String endValue;
    String message;
    @ManyToOne
    @JoinColumn(name = "rfid_number", nullable = false)
    Rfid rfid;
    @ManyToOne
    @JoinColumn(name = "port_number", nullable = false)
    Port port;
}
