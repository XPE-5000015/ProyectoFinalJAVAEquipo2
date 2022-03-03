package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "booking_person")
public class Booking_person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_person_id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Hotel_booking hotel_booking;

    @ManyToOne
    @JoinColumn(name = "dni")
    private Person personBooking;
}
