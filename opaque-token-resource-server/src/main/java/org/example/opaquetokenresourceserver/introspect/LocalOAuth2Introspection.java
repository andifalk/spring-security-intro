package org.example.opaquetokenresourceserver.introspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@Profile("local-testing")
@RestController
@RequestMapping("/local")
public class LocalOAuth2Introspection {

    private static final Logger LOG = LoggerFactory.getLogger(LocalOAuth2Introspection.class);

    @PostMapping(value = "/introspect", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public IntrospectionResult introspect(@AuthenticationPrincipal(errorOnInvalidType = true) User user, String token) {

        LOG.info("Token {}", token);
        LOG.info("User {}", user);

        return new IntrospectionResult(true,
                user.getUsername(), user.getUsername(), "openid profile email", user.getUsername(),
                "myaudience", "http://localhost:8080",
                Instant.now().plus(Duration.ofHours(1)).getEpochSecond(),
                Instant.now().getEpochSecond());
    }
}
