package be.kdg.sa.deliveryservice.api;

import be.kdg.sa.deliveryservice.application.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/security")
public class SecurityController {
    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/JwtToken")
    public ResponseEntity<String> getJwtAccesToken() {
        String accesToken = securityService.getJwtAccesToken();
        return ResponseEntity.ok(accesToken);
    }
}
