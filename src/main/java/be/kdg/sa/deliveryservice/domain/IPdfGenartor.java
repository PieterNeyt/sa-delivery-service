package be.kdg.sa.deliveryservice.domain;

import be.kdg.sa.deliveryservice.domain.payout.Payout;

import java.util.List;

public interface IPdfGenartor {
    byte[] generatorPayoutsOverview(List<Payout> payouts);
}
