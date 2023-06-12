package com.example.chargestation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Stream;

@JsonTest(properties = "app.elastic.index=off")
@Import(DateParserTests.TestDateConverterConfig.class)
public class DateParserTests {

    @TestConfiguration
    @EnableConfigurationProperties(AppProps.class)
    public static class TestDateConverterConfig extends DateParserConfig {
        @Override
        @Bean
        public Function<String, Date> dateParser(AppProps appProps) {
            return super.dateParser(appProps);
        }
    }

    @Autowired Function<String, Date> dateParser;

    static Stream<Arguments> dates() {
        return Stream.of(Arguments.of("23.11.1988", LocalDateTime.of(1988, 11, 23, 0, 0)),
            Arguments.of("23-11-1988", LocalDateTime.of(1988, 11, 23, 0, 0)),
            Arguments.of("1988/11/23", LocalDateTime.of(1988, 11, 23, 0, 0)),
            Arguments.of("1988/11/23 13:10", LocalDateTime.of(1988, 11, 23, 13, 10)));
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("com.example.chargestation.DateParserTests#dates")
    void shouldParseGivenDate(String dateStr, LocalDateTime mustEqualTo) {
        Assertions.assertEquals(dateParser.apply(dateStr), Date.from(mustEqualTo.toInstant(ZoneOffset.UTC)));
    }
}
