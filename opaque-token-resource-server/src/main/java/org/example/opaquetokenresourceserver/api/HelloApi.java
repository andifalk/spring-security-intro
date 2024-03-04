package org.example.opaquetokenresourceserver.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloApi {

    @GetMapping("/hello")
    public String sayHello(
            @AuthenticationPrincipal(errorOnInvalidType = true)
            OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        return "Hello " + authenticatedPrincipal.getName();
    }
}
