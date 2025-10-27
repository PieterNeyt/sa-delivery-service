package be.kdg.sa.deliveryservice;


import be.kdg.sa.deliveryservice.domain.courier.Courier;
import be.kdg.sa.deliveryservice.infrastructure.DbCourierRepository;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaCourierEntity;
import be.kdg.sa.deliveryservice.infrastructure.jpa.JpaCourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbCourierRepositoryTest {

    @Mock
    private JpaCourierRepository jpaCourierRepository;

    @InjectMocks
    private DbCourierRepository dbCourierRepository;

    @Test
    void findById_mapsToDomain() {
        UUID id = UUID.randomUUID();
        Courier courier = new Courier("John", "Doe", "john@doe.com", "0123456", "Street", "BE12");
        JpaCourierEntity entity = JpaCourierEntity.fromDomain(courier);

        when(jpaCourierRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Courier> result = dbCourierRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo(courier.getFirstName());
    }

    @Test
    void hasActiveDelivery_callsJpa() {
        UUID id = UUID.randomUUID();
        when(jpaCourierRepository.hasActiveDelivery(id)).thenReturn(true);

        boolean result = dbCourierRepository.hasActiveDelivery(id);
        assertThat(result).isTrue();
    }
}
