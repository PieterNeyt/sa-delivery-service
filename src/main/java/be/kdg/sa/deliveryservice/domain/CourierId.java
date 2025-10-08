package be.kdg.sa.deliveryservice.domain;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record CourierId(UUID id) {
    public CourierId {
        Assert.notNull(id, "id cannot be null");
    }

    public static CourierId create() {
        return new CourierId(UUID.randomUUID());
    }

}
