package com.example.chargestation.repository.schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rfid")
@Entity
public class Rfid {
    @Id @Column(name = "number") String serialNumber;
    @Column(name = "name") String name;
    @OneToOne @JoinColumn(name = "vehicle_number", unique = true, updatable = false) Vehicle vehicle;
}
