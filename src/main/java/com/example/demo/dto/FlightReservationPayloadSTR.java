package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationPayloadSTR {
    String dateFrom;
    String dateTo;
    String origin;
    String destination;
    String flightNumber;
    Integer seats;
    String seatType;
    List<PersonDTO> people;
    PaymentMethodDTO paymentMethod;
}
