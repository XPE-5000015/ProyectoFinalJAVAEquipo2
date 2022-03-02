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
@Table(name = "hotels")
public class Hotel {
    @Id
    private String hotelCode;
    private String name;
    private String place;
    private String roomType;
    private Double roomPrice;
    private Date disponibilityDateFrom;
    private Date disponibilityDateTo;
    private Boolean isBooking;

    @OneToMany(mappedBy = "hotel")
    private List<Hotel_booking> hotel_bookings;
}