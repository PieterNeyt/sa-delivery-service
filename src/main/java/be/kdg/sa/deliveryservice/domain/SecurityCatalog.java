package be.kdg.sa.deliveryservice.domain;

import java.util.Optional;

public interface SecurityCatalog {
    Optional<String> getAccesToken();
}
