    package be.kdg.sa.deliveryservice.infrastructure.jpa;

    import be.kdg.sa.deliveryservice.domain.Courier;
    import be.kdg.sa.deliveryservice.domain.CourierId;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.Getter;

    import java.util.UUID;

    @Entity
    @Getter
    @Table(name="courier")
    public class JpaCourierEntity {

        @Id
        private UUID id;

        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        private String phoneNumber;

        @Column(nullable = false)
        private String address;

        @Column(nullable = false)
        private String IBAN;

        protected JpaCourierEntity() {
        }

        public JpaCourierEntity(UUID id, String firstName, String lastName, String email, String phoneNumber, String address, String IBAN) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.IBAN = IBAN;
        }

        public Courier toDomain() {
            return new Courier(new CourierId(id),firstName, lastName, email, phoneNumber, address, IBAN);
        }

        public static JpaCourierEntity fromDomain(Courier courier) {
            return new JpaCourierEntity(courier.getId().id(),
                    courier.getFirstName(),
                    courier.getLastName(),
                    courier.getEmail(),
                    courier.getPhoneNumber(),
                    courier.getAddress(),
                    courier.getIBAN());
        }
    }
