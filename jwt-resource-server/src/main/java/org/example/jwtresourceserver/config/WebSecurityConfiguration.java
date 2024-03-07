package org.example.jwtresourceserver.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration {

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    public WebSecurityConfiguration(OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }

    @Profile("local-testing")
    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec("12345678901234567890123456789012".getBytes(), HMAC_SHA512);
    }

    @Profile("local-testing")
    @Bean
    public JwtDecoder jwtDecoderWithSecretKey(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Profile("!local-testing")
    @Bean
    public JwtDecoder jwtDecoderWithPublicKey() {
        return NimbusJwtDecoder.withJwkSetUri(oAuth2ResourceServerProperties.getJwt().getJwkSetUri()).build();
    }

    @Profile("local-testing")
    @Bean
    @Order(1)
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/local/*"
                );
    }

    @Order(2)
    @Bean
    public SecurityFilterChain actuatorSecurity(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .securityMatcher(new AntPathRequestMatcher("/v3/**"))
                .securityMatcher(new AntPathRequestMatcher("/swagger-ui.html"))
                .authorizeHttpRequests(
                        req -> req.anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults()).formLogin(withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity httpSecurity, JwtDecoder jwtDecoder) throws Exception {

        return httpSecurity.authorizeHttpRequests(
                        req -> req.anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .oauth2ResourceServer(rs -> rs.jwt(
                        jwt -> {
                            jwt.decoder(jwtDecoder);
                        })).build();
    }
}
