package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotel_bookings")
public class Hotel_booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_id;
    private String userName;
    private Date dateFrom;
    private Date dateTo;
    private String destination;
    private String hotelCode;
    private Integer peopleAmount;
    private String roomType;
    private Integer paymentMethod_id;

    @ManyToOne
    @JoinColumn(name = "hotelCode", insertable = false, updatable = false)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "paymentMethod_id", insertable = false, updatable = false)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "hotel_booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking_person> booking_people;
}
