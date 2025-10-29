package be.kdg.sa.deliveryservice.domain.payout;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PayoutRepository {
    List<Payout> findByCourierId(UUID courierId);
    void save(Payout payout);

    List<Payout> findAllPayoutsInBetween(Date startDate, Date endDate);
}
