package com.example.chargestation;

import com.example.chargestation.repository.PointRepository;
import com.example.chargestation.repository.PortRepository;
import com.example.chargestation.repository.SessionRepository;
import com.example.chargestation.repository.VehicleDocRepository;
import com.example.chargestation.repository.schema.Customer;
import com.example.chargestation.repository.schema.Port;
import com.example.chargestation.repository.schema.Session;
import com.example.chargestation.repository.schema.VehicleDoc;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AppUseCases {
    PortRepository portRepository;
    PointRepository pointRepository;
    SessionRepository chargeSessionRepository;
    Function<String, Date> dateParser;
    VehicleDocRepository vehicleDocRepository;
    AppProps appProps;

    public AppProps.VersionProps getVersion() {
        return appProps.getVersion();
    }

    @Transactional
    public Port handlePortAttach(String portId, String pointId) {
        if (portRepository.findById(portId).isPresent()) {
            throw new WrongParamsException("Duplicate port id");
        }
        var point = pointRepository.findById(pointId);
        if (point.isEmpty()) {
            throw new WrongParamsException("Charge point not found");
        }
        return point.map(cs -> portRepository.save(new Port(portId, cs))).orElseThrow();
    }

    @Transactional
    public void handleSession(Customer initiator, String portNumber, String value) {
        if (initiator == null) {
            throw new WrongParamsException("User is not a customer");
        }

        var rfid = initiator.getRfid();
        var port = portRepository.findById(portNumber).orElse(null);

        if (port == null) {
            throw new WrongParamsException("Bad port");
        } else if (rfid == null) {
            throw new WrongParamsException("Bad rfid");
        } else if (rfid.getVehicle() == null) {
            throw new WrongParamsException("Vehicle must be registered with rfid");
        }

        var activeSessionCount = chargeSessionRepository.countSessionsByRfidAndPortAndEndDateIsNull(rfid, port);
        var activeSessionOnPort = chargeSessionRepository.findSessionByPortAndEndDateIsNull(port);

        if (activeSessionOnPort != null) {
            if (!activeSessionOnPort.getRfid().getSerialNumber().equals(rfid.getSerialNumber())) {
                throw new WrongParamsException("Bad port");
            }
            activeSessionOnPort.setEndDate(new Date());
            activeSessionOnPort.setEndValue(value);
            chargeSessionRepository.save(activeSessionOnPort);
            return;
        }

        if (activeSessionCount == 0) {
            chargeSessionRepository.save(Session.builder().startDate(new Date()).port(port).rfid(rfid).startValue(value).build());
            return;
        }

        throw new WrongParamsException("Bad port");
    }

    @Transactional(readOnly = true)
    public Iterable<Session> searchSessions(String startDateFrom, String startDateTo) {
        if (startDateFrom == null && startDateTo == null) {
            return chargeSessionRepository.findAll();
        }
        var sdFrom = dateParser.apply(startDateFrom);
        var sdTo = dateParser.apply(startDateTo);
        if (sdFrom.after(sdTo)) {
            throw new WrongParamsException("from > to");
        }
        return chargeSessionRepository.findSessionsByStartDateBetween(sdFrom, sdTo);
    }

    @Transactional(readOnly = true)
    public Iterable<VehicleDoc> searchVehicles(String registrationPlate, String name) {
        return vehicleDocRepository.findVehicleSearchItemsByRegistrationPlateContainingIgnoreCaseAndNameContainingIgnoreCase(
            registrationPlate, name).stream().toList();
    }
}
