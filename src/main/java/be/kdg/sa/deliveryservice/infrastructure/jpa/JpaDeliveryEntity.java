package be.kdg.sa.deliveryservice.infrastructure.jpa;

import be.kdg.sa.deliveryservice.api.CourierDto;
import be.kdg.sa.deliveryservice.domain.*;
import jakarta.persistence.*;
import lombok.Getter;


import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Table(name = "delivery")
public class JpaDeliveryEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID orderId;

    @Column()
    private UUID courierId;

    @Column
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @Column
    private Date startDelivery;
    @Column
    private Date endDelivery;

    protected JpaDeliveryEntity() {
    }

    public JpaDeliveryEntity(UUID id, UUID orderId, UUID courierId, DeliveryStatus deliveryStatus, Date startDelivery, Date endDelivery) {
        this.id = id;
        this.orderId = orderId;
        this.courierId = courierId;
        this.deliveryStatus = deliveryStatus;
        this.startDelivery = startDelivery;
        this.endDelivery = endDelivery;
    }

    public static JpaDeliveryEntity fromDomain(Delivery delivery) {
        return new JpaDeliveryEntity(delivery.getDeliveryId().id(),
                delivery.getOrderId().id(),
                delivery.getCourierId().id(),
                delivery.getDeliveryStatus(),
                delivery.getStartDelivery(),
                delivery.getEndDelivery());
    }

    public Delivery toDomain() {
        CourierId courierId1 = courierId==null ?
                null:new CourierId(courierId);
        Delivery delivery = new Delivery(
                new DeliveryId(id),
                new OrderId(orderId),
                courierId1,
                startDelivery,
                endDelivery,
                deliveryStatus
        );
        return delivery;
    }
}
