package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight_reservations")
public class Flight_reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservation_id;
    private String userName;
    private Date goingDate;
    private Date returnDate;
    private String origin;
    private String destination;
    private String flightNumber;
    private Integer seats;
    private String seatType;
}
