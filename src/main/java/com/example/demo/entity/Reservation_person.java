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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Reservation_person;
    String dni;
}
