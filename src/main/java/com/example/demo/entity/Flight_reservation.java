package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight_reservations")
public class Flight_reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer reservation_id;
    String userName;
    Date goingDate;
    Date returnDate;
    String origin;
    String destination;
    String flightNumber;
    Integer seats;
    String seatType;
}
