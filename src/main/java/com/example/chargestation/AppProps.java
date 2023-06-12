package com.example.chargestation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@ConfigurationProperties(prefix = "app.props")
public class AppProps {
    List<String> patterns;
    VersionProps version;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @ConfigurationProperties(prefix = "app.props.version")
    public static class VersionProps {
        String apiVersion;
        String appVersion;
    }
}
