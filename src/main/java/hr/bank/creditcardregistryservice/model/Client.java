package hr.bank.creditcardregistryservice.model;

import hr.bank.creditcardregistryservice.utils.CustomUtils;
import hr.bank.creditcardregistryservice.validator.constraints.RegularName;
import hr.bank.creditcardregistryservice.validator.constraints.RegularPersonalIdentificationNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigInteger;


@Entity
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @Column(name = "id", unique = true)
    private BigInteger id;

    @Column(name = "identification_number")
    @RegularPersonalIdentificationNumber
    private String personalIdentificationNumber;

    @Column(name = "first_name", nullable = false, length = 20)
    @NotEmpty(message = "Please provide first name")
    @RegularName(message = "First Name format is not supported. " +
            "First name should start with capital letter, have at least 2 characters" +
            "and have no special characters/numbers")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    @NotEmpty(message = "Please provide last name")
    @RegularName(message = "Last Name format is not supported. " +
            "Last name should start with capital letter, have at least 2 characters" +
            "and have no special characters/numbers")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private CreditCardStatus status;

    public enum CreditCardStatus {
        ACTIVE,
        INACTIVE,
    }

    public Client(String personalIdentificationNumber, String firstName, String lastName, CreditCardStatus status) {
        this.personalIdentificationNumber = personalIdentificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.id = CustomUtils.personalIdentificationNumberToId(this.personalIdentificationNumber);
    }



}

