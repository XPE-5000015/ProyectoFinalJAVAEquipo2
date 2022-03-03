package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    //private String flightNumber;
    private Integer seats;
    private String seatType;
    //private Integer paymentMethod_id;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "paymentMethod_id")
    private PaymentMethod paymentMethodReservation;

    @ManyToOne
    @JoinColumn(name = "flightNumber")
    private Flight flight;

    @OneToMany(mappedBy = "flight_reservation")
    private List<Reservation_person> reservation_people;
}
