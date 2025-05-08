package com.microcommerce.payments.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing credit card information stored in the database.
 * This class contains sensitive payment information and should be handled with appropriate security measures
 * in production contexts. The security measures will not be considered because this is an educational scenario.
 */
@Entity
@Table(name = "credit_cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardInfoModel {
    /**
     * Unique identifier for the credit card information record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the credit card holder as it appears on the card
     */
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    /**
     * Credit card number (should be stored encrypted in a real scenario)
     */
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    /**
     * Card expiration date in MM/YY format
     */
    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    /**
     * Card verification value (CVV) - security code on the back of the card
     */
    @Column(name = "cvv", nullable = false)
    private String cvv;
}
