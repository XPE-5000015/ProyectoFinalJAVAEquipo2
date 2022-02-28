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
@IdClass(Booking_person.class)
public class Booking_person implements Serializable {

    @Id
    private Integer booking_id;
    @Id
    private String dni;

    @ManyToOne
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    private Hotel_booking hotel_booking;

    @ManyToOne
    @JoinColumn(name = "dni", insertable = false, updatable = false)
    private Person person;
}
