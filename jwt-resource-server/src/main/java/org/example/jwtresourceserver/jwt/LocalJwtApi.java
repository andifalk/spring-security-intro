package org.example.jwtresourceserver.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Profile("local-testing")
@RestController
@RequestMapping("/local")
public class LocalJwtApi {

    private static final Logger LOG = LoggerFactory.getLogger(LocalJwtApi.class);

    private final JWSSigner signer;

    public LocalJwtApi(SecretKey secretKey) throws KeyLengthException {
        this.signer = new MACSigner(secretKey);
    }

    @GetMapping("/jwt")
    public String createLocalJwtForTesting() throws JOSEException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("iss", "test_issuer");
        payload.put("exp", Math.abs(System.currentTimeMillis() / 1000) + (5 * 60));
        payload.put("aud", new String[] {"hello-service"});
        payload.put("sub", "jdoe");
        payload.put("scope", "openid email profile");
        payload.put("groups", new String[] {"user"});
        payload.put("preferred_username", "John Doe");
        payload.put("given_name", "John");
        payload.put("family_name", "Doe");
        payload.put("email", "john.doe@example.com");
        payload.put("email_verified", true);
        payload.put("name", "John Doe");

        String jwt = createJwt(payload);
        LOG.info("Created jwt {}", jwt);
        return jwt;
    }

    private String createJwt(Map<String, Object> payload) throws JOSEException {

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.HS256).build(),
                new Payload(payload));

        // Compute the RSA signature
        jwsObject.sign(signer);

        // To serialize to compact form, produces something like
        // eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L
        // mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd
        // maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7
        // -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
        return jwsObject.serialize();
    }
}
