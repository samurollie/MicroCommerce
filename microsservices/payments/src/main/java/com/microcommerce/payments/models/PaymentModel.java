package com.microcommerce.payments.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


}
