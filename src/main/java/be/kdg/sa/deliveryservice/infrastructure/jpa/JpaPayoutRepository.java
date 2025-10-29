package be.kdg.sa.deliveryservice.infrastructure.jpa;

import be.kdg.sa.deliveryservice.domain.payout.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface JpaPayoutRepository extends JpaRepository<JpaPayoutEntity, UUID> {

    List<JpaPayoutEntity> findByCourierId(UUID courierId);

    List<JpaPayoutEntity> findAllByPayoutDateBetween(Date payoutDateAfter, Date payoutDateBefore);
}
