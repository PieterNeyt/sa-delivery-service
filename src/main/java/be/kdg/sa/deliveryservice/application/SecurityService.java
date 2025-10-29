package be.kdg.sa.deliveryservice.application;


import be.kdg.sa.deliveryservice.domain.NotFoundException;
import be.kdg.sa.deliveryservice.domain.SecurityCatalog;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final SecurityCatalog securityCatalog;

    public SecurityService(SecurityCatalog securityCatalog) {
        this.securityCatalog = securityCatalog;
    }

    public String getJwtAccesToken() {
        return securityCatalog.getAccesToken()
                .orElseThrow(() -> new NotFoundException("Acces token not found"));
    }
}
