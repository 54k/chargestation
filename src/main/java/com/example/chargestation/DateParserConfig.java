package com.example.chargestation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Configuration
public class DateParserConfig {
    @Bean
    public Function<String, Date> dateParser(AppProps appProps) {
        var formatters = appProps.getPatterns().stream()
            .map(pat -> new DateTimeFormatterBuilder().appendPattern(pat).parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).toFormatter()).toList();
        return (d) -> {
            LocalDateTime parsed = null;
            for (var dtf : formatters) {
                try {
                    parsed = LocalDateTime.parse(d, dtf);
                    break;
                } catch (Exception parseException) {
                    log.debug("Error while parsing:", parseException);
                }
            }
            if (parsed == null) {
                throw new WrongParamsException("Invalid date format");
            }
            return Date.from(parsed.toInstant(ZoneOffset.UTC));
        };
    }

}
