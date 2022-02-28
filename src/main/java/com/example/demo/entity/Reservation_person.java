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
@IdClass(Reservation_person.class)
public class Reservation_person implements Serializable {
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
