package be.kdg.sa.deliveryservice.infrastructure.securitycatalog;

import be.kdg.sa.deliveryservice.domain.SecurityCatalog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.catalog.base-url}")
    private String baseUrl;

    @Value("${security.catalog.client-id}")
    private String clientId;

    @Value("${security.catalog.client-secret}")
    private String clientSecret;

    @Value("${security.catalog.username}")
    private String username;

    @Value("${security.catalog.password}")
    private String password;

    @Value("${security.catalog.grant-type}")
    private String grantType;

    @Value("${security.catalog.scope}")
    private String scope;

    public ExternalSecurityCatalog(@Qualifier("SecurityCatalogApi") final RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<String> getAccesToken() {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", grantType);
        formData.add("scope", scope);

        Map<String, Object> response = restClient
                .post()
                .uri(baseUrl)
                .body(formData)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        String accessToken = response != null ? (String) response.get("access_token") : null;
        return Optional.ofNullable(accessToken);
    }
}
