package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "paymentMethods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentMethod_id;
    private String type;
    private String number;
    private Integer dues;

    @OneToMany(mappedBy = "paymentMethodBooking")
    private List<Hotel_booking> hotel_bookings;

    @OneToMany(mappedBy = "paymentMethodReservation")
    private List<Flight_reservation> flight_reservations;
}
