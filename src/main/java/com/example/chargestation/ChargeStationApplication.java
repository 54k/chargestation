package com.example.chargestation;

import com.example.chargestation.repository.VehicleDocRepository;
import com.example.chargestation.repository.VehicleRepository;
import com.example.chargestation.repository.schema.VehicleDoc;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChargeStationApplication {

    @ConditionalOnProperty(value = "app.elastic.index", havingValue = "true")
    @Slf4j
    @Component
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class AppRunner implements ApplicationRunner {
        VehicleRepository vehicleEntityRepository;
        VehicleDocRepository vehicleDocumentRepository;

        @Override
        public void run(ApplicationArguments args) {
            log.info("Indexing vehicles DB");
            vehicleEntityRepository.findAll()
                .forEach(v -> vehicleDocumentRepository.save(new VehicleDoc(v.getRegistrationPlate(), v.getName())));
            log.info("Indexing vehicles DB done");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ChargeStationApplication.class, args);
    }
}
