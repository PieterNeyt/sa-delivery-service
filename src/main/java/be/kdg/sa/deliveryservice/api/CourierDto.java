package be.kdg.sa.deliveryservice.api;

import be.kdg.sa.deliveryservice.domain.Courier;

import java.util.UUID;

public record CourierDto(UUID id, String firstName, String lastName,
                         String email, String phonenumber,
                         String address, String IBAN) {

    public static CourierDto from(Courier courier) {
        return new CourierDto(courier.getId().id(),
                courier.firstName,
                courier.lastName,
                courier.email,
                courier.phoneNumber,
                courier.address,
                courier.IBAN);
    }
}
