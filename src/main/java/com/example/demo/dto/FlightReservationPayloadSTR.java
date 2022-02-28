package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationPayloadSTR {
    private String dateFrom;
    private String dateTo;
    private String origin;
    private String destination;
    private String flightNumber;
    private Integer seats;
    private String seatType;
    private List<PersonDTO> people;
    private PaymentMethodDTO paymentMethod;
}
