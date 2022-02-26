package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_person")
public class Reservation_person {
    @Id
    private Integer reservation_id;
    @Id
    private String dni;

    @ManyToOne
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private Flight_reservation flight_reservation;

    @ManyToOne
    @JoinColumn(name = "dni", insertable = false, updatable = false)
    private Person person;
}
