package com.example.chargestation;

import com.example.chargestation.repository.schema.Port;
import com.example.chargestation.repository.schema.VehicleDoc;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

@RequestMapping("api/v1/")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AppController {
    AppUseCases appUseCases;

    @Operation(description = "Get app version properties")
    @GetMapping(path = "/version")
    AppProps.VersionProps version() {
        return appUseCases.getVersion();
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PortAttachRequest {
        @NonNull String portNumber;
        @NonNull String chargeStationNumber;
    }

    @Operation(description = "Connect new port to charge point")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/port")
    Port attachPort(@RequestBody PortAttachRequest evt) {
        return appUseCases.handlePortAttach(evt.portNumber, evt.chargeStationNumber);
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class HandleSessionRequest {
        @NonNull String portNumber;
        @NonNull String value;
        String message;
    }

    @AllArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class HandleSessionReply {
        String message;
    }

    @Operation(description = "Handle session event")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/session")
    HandleSessionReply handleSession(@RequestBody HandleSessionRequest evt, @AuthenticationPrincipal AppUserDetails userDetails) {
        appUseCases.handleSession(userDetails.getCustomer(), evt.portNumber, evt.value);
        return new HandleSessionReply("ok");
    }

    @AllArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SearchSessionsReply {
        @AllArgsConstructor
        @Data
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class Session {
            String id;
            Date startDate;
            Date endDate;
            String startValue;
            String endValue;
            String message;
            String rfid;
            String port;
        }

        List<Session> sessions;
    }

    @Operation(description = "Search session events by date range")
    @GetMapping(path = "/session")
    List<SearchSessionsReply> searchSessions(@RequestParam(name = "from", required = false) String from,
        @RequestParam(name = "to", required = false) String to)
    {
        return StreamSupport.stream(appUseCases.searchSessions(from, to).spliterator(), false).map(
            session -> new SearchSessionsReply(List.of(
                new SearchSessionsReply.Session(session.getId(), session.getStartDate(), session.getEndDate(),
                    session.getEndValue(), session.getEndValue(), session.getMessage(), session.getRfid().getSerialNumber(),
                    session.getPort().getNumber())))).toList();
    }

    @Operation(description = "Search vehicles by license plate and name")
    @GetMapping(path = "/vehicle")
    Iterable<VehicleDoc> searchVehicles(
        @RequestParam(name = "plate", required = false, defaultValue = "") String registrationPlate,
        @RequestParam(name = "name", required = false, defaultValue = "") String name)
    {
        return appUseCases.searchVehicles(registrationPlate, name);
    }
}
