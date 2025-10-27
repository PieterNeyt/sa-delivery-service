package be.kdg.sa.deliveryservice.domain;


import lombok.Getter;

@Getter
public enum DeliveryStatus {
    AVAILABLE("We zoeken een coerier."),
    ACCEPTED("We hebben een coerier gevonden, restaurant is bezig met voorbereiding van uw order."),
    PENDING_PICKUP("wait for pick up"),
    PICKED_UP("De coerier is onderweg met je bestelling."),
    DELIVERD("Je eten is er"),
    CANCELLED("De order is gecancelled.");

    private final String text;

    DeliveryStatus(String text) {
        this.text = text;
    }
}