package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "booking_person")
public class Booking_person {

    @Id
    private Integer booking_id;
    @Id
    private String dni;

    @ManyToOne
    @JoinColumn(name = "dni", insertable = false, updatable = false)
    private Person person;
}
