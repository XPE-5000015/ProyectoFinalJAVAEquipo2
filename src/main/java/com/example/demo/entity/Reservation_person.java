package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_person")
public class Reservation_person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservation_person_id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Flight_reservation flight_reservation;

    @ManyToOne
    @JoinColumn(name = "dni")
    private Person personReservation;
}
