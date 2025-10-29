package be.kdg.sa.deliveryservice.infrastructure.securitycatalog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalSecurityCatalogConfig {
    @Bean("SecurityCatalogApi")
    RestClient restaurantCatalogRestTemplate(@Value("${security-catalog-api.url}") final String url) {
        return RestClient.create(url);
    }
}
