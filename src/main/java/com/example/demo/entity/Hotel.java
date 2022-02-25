package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotels")
public class Hotel {
    String hotelCode;
    String name;
    String place;
    String roomType;
    Double roomPrice;
    Date disponibilityDateFrom;
    Date disponibilityDateTo;
    Boolean isBooking;
}