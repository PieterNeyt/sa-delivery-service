package be.kdg.sa.deliveryservice.domain.delivery;

import be.kdg.sa.deliveryservice.infrastructure.handler.DeliveryResponse;

public interface IDeliveryMessagePublisher {

     void sendClaimedResponse(DeliveryResponse response);

     void sendPickedUpResponse(DeliveryResponse response) ;

     void sendDeliveredResponse(DeliveryResponse response);
}
