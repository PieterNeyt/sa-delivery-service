package be.kdg.sa.deliveryservice.infrastructure;

import be.kdg.sa.deliveryservice.domain.payout.Payout;
import be.kdg.sa.deliveryservice.domain.payout.PayoutRepository;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaPayoutRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DbPayoutRepository implements PayoutRepository {
    private final JpaPayoutRepository jpaPayoutRepository;

    public DbPayoutRepository(JpaPayoutRepository jpaPayoutRepository) {
        this.jpaPayoutRepository = jpaPayoutRepository;
    }

    @Override
    public List<Payout> findByCourierId(UUID courierId) {
        return this.jpaPayoutRepository.findByCourierId(courierId);
    }

    @Override
    public void save(Payout payout) {
        this.jpaPayoutRepository.save(JpaPayoutEntity.fromDomain(payout));
    }
}
