package be.kdg.sa.deliveryservice.domain;


import lombok.Getter;

@Getter
public enum DeliveryStatus {
    AVAILABLE("We zoeken een coerier."),
    PENDING("We zoeken een coerier"),
    ACCEPTED("We hebben een coerier gevonden, restaurant is bezig met voorbereiding van uw order."),
    IN_ROUTE("De coerier is onderweg met je bestelling."),
    DELIVERED("Je eten is er");

    private final String text;

    DeliveryStatus(String text) {
        this.text = text;
    }
}