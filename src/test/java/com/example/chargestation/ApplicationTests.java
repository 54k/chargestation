package com.example.chargestation;

import com.example.chargestation.repository.PortRepository;
import com.example.chargestation.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@Sql(statements = "truncate session")
@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:///",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",})
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Testcontainers
public class ApplicationTests {
    @Container @SuppressWarnings("rawtypes") public static PostgreSQLContainer dbContainer =
        new PostgreSQLContainer("postgres:11.1").withUsername("user").withPassword("welcome");
    @Container @SuppressWarnings("rawtypes") public static GenericContainer elastic =
        new GenericContainer("elasticsearch:8.8.0").withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false").withExposedPorts(9200);

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> String.format("%s:%s", elastic.getHost(), elastic.getFirstMappedPort()));
    }

    @Autowired MockMvc mockMvc;
    @Autowired AppProps appProps;
    @Autowired ObjectMapper objectMapper;
    @Autowired PortRepository portRepository;
    @Autowired SessionRepository sessionRepository;

    @Test
    public void shouldReturnAppVersion200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/version").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(content().json("{\"apiVersion\":\"1\",\"appVersion\":\"0.0.1-SNAPSHOT\"}", false))
            .andDo(document("version"));
    }

    @WithUserDetails
    @Test
    public void shouldHandleSession200() throws Exception {
        var session = new AppController.HandleSessionRequest("1", "1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/session").content(objectMapper.writeValueAsString(session))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        Assertions.assertNotNull(sessionRepository.findSessionByPortAndEndDateIsNull(portRepository.findById("1").orElseThrow()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/session").content(objectMapper.writeValueAsString(session))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        Assertions.assertNull(sessionRepository.findSessionByPortAndEndDateIsNull(portRepository.findById("1").orElseThrow()));
        Assertions.assertEquals(sessionRepository.count(), 1);
    }

    @WithUserDetails("admin")
    @Test
    public void shouldHandleSession403() throws Exception {
        var session = new AppController.HandleSessionRequest("1", "1");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/session").content(objectMapper.writeValueAsString(session))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        Assertions.assertEquals(sessionRepository.count(), 0);
    }

    @WithUserDetails("admin")
    @Test
    public void shouldAttachPort200() throws Exception {
        var port = new AppController.PortAttachRequest("2", "1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/port").content(objectMapper.writeValueAsString(port))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        Assertions.assertTrue(portRepository.findById("2").isPresent());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/port").content(objectMapper.writeValueAsString(port))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @WithUserDetails
    @Test
    public void shouldAttachPort403() throws Exception {
        var port = new AppController.PortAttachRequest("2", "1");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/port").content(objectMapper.writeValueAsString(port))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @WithUserDetails("admin")
    @Test
    public void shouldReturnChargeSessions200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/session").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/session").queryParam("from", "2020/03/01")
                    .queryParam("to", "2025-03-05 13:10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/session").queryParam("from", "2025-03-05 13:10")
                    .queryParam("to", "2020/03/01")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @WithUserDetails
    @Test
    public void shouldReturnChargeSessions403() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/session").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}
