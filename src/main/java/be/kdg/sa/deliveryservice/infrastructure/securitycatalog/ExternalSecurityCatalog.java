package be.kdg.sa.deliveryservice.infrastructure.securitycatalog;

import be.kdg.sa.deliveryservice.domain.SecurityCatalog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Component
public class ExternalSecurityCatalog implements SecurityCatalog {

    private final RestClient restClient;

    public ExternalSecurityCatalog(@Qualifier("SecurityCatalogApi") final RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public Optional<String> getAccesToken() {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "backend-client");
        formData.add("client_secret", "pBAtElyMTHBp0IBEwO8G8h7bt6Jb4khZ");
        formData.add("username", "owner");
        formData.add("password", "password");
        formData.add("grant_type", "password");
        formData.add("scope", "openid");

        Map<String, Object> response = restClient
                .post()
                .uri("")
                .body(formData)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        String accessToken = response != null ? (String) response.get("access_token") : null;
        return Optional.ofNullable(accessToken);

    }
}
