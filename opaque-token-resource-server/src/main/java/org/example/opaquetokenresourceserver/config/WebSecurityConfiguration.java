package org.example.opaquetokenresourceserver.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration {

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    public WebSecurityConfiguration(OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }

    /*
    @Bean
    @Order(1)
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/local/*"
                );
    }*/

    @Profile("local-testing")
    @Order(2)
    @Bean
    public SecurityFilterChain localSecurity(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .securityMatcher("/local/*")
                .csrf((AbstractHttpConfigurer::disable))
                .authorizeHttpRequests(
                        req -> req.anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()).formLogin(AbstractHttpConfigurer::disable).build();
    }

    @Profile("local-testing")
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder().username("test-client").password(passwordEncoder().encode("secret")).roles("USER").build()
        );
    }

    @Profile("local-testing")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.authorizeHttpRequests(
                        req -> req.anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .oauth2ResourceServer(rs -> rs.opaqueToken(
                        op -> {
                            op.introspectionUri(oAuth2ResourceServerProperties.getOpaquetoken().getIntrospectionUri());
                            op.introspectionClientCredentials(
                                    oAuth2ResourceServerProperties.getOpaquetoken().getClientId(),
                                    oAuth2ResourceServerProperties.getOpaquetoken().getClientSecret());
                        })).build();
    }
}
