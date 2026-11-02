package com.tickets.eventify.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import java.util.Map;

@RestController
@RequestMapping("/jwt")
@Profile("dev")
@Tag(name = "JWT Token Controller", description = "Controller for obtaining JWT tokens in development environment")
// This controller is only active in the "dev" profile, allowing developers to obtain JWT tokens for testing purposes.
public class JwtTokenController {

    @Value("${app.dev.keycloak.token-url}")
    private String TOKEN_URL ;

    @Value("${app.dev.keycloak.client-id}")
    private String CLIENT_ID ;

    private final RestClient restClient = RestClient.create();

    public record UserNameAndPasswordRequest(String username, String password) {}

    @PostMapping("/token")
    @Operation(summary = "Get JWT Token")
    public String getToken(@RequestBody UserNameAndPasswordRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", CLIENT_ID);
        form.add("username", request.username());
        form.add("password", request.password());

        Map<String, Object> response = restClient.post()
                .uri(TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        return  response.get("access_token").toString();
    }
}

