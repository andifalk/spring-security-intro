package org.example.opaquetokenresourceserver.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.opaqueToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"local-testing", "test"})
@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT)
class HelloApiEnd2EndTest {

    @Test
    void sayHello(@Autowired WebTestClient webClient) {
        webClient
                .get().uri("/api/hello")
                .header("Authorization", "Bearer test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello test");
    }

    @Test
    void sayHelloUnauthorized(@Autowired WebTestClient webClient) {
        webClient
                .get().uri("/api/hello")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}