package be.kdg.sa.deliveryservice.domain.payout;

import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@ValueObject
public record PayoutId(UUID id) {
    public PayoutId {
        Assert.notNull(id, "id cannot be null");
    }

    public static PayoutId create() {
        return new PayoutId(UUID.randomUUID());
    }

}
