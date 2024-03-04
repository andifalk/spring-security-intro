package org.example.opaquetokenresourceserver.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.opaqueToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloApiIntegrationTest {

    @Test
    void sayHello(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/hello").with(opaqueToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello user"));
    }

    @Test
    void sayHelloUnauthorized(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/hello"))
                .andExpect(status().isUnauthorized());
    }
}