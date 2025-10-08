package be.kdg.sa.deliveryservice.domain;

import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.annotation.Entity;


@Getter
@ToString
@Entity
public class Courier {
    public CourierId id;

    public String firstName;
    public String lastName;

    public String email;
    public String phoneNumber;
    public String address;

    public String IBAN;
    public Courier(String firstName, String lastName,
                   String email, String phoneNumber, String address,
                   String IBAN) {

        this.id = CourierId.create();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
    }

    public Courier(CourierId id, String firstName, String lastName, String email, String phoneNumber, String address, String IBAN) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
    }
}
