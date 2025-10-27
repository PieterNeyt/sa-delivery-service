package be.kdg.sa.deliveryservice.infrastructure;

import be.kdg.sa.deliveryservice.application.CourierRepository;
import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaCourierEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaCourierRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DbCourierRepository implements CourierRepository {
    private final JpaCourierRepository jpaCourierRepository;

    public DbCourierRepository(JpaCourierRepository jpaCourierRepository) {
        this.jpaCourierRepository = jpaCourierRepository;
    }
    @Override
    public Optional<Courier> findById(UUID id) {
        return this.jpaCourierRepository.findById(id).map(JpaCourierEntity::toDomain);
    }

    @Override
    public boolean hasActiveDelivery(UUID id) {
        return this.jpaCourierRepository.hasActiveDelivery(id);
    }
}
