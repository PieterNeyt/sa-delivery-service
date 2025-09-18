package be.kdg.sa.deliveryservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Courier {
    public UUID courierId;

    public String firstName;
    public String lastName;

    public String email;
    public String phoneNumber;
    public String address;

    public String IBAN;

    public Delivery currentDelivery;
    public List<Delivery> pastDeliverys;

    public Courier(String firstName, String lastName,
                   String email, String phoneNumber, String address,
                   String IBAN, Delivery currentDelivery) {

        this.courierId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
        this.currentDelivery = currentDelivery;
        this.pastDeliverys = new ArrayList<>();
    }
}
