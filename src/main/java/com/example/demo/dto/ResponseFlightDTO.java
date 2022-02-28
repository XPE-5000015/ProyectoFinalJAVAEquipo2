package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFlightDTO {
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationResponseDTO flightReservation;
    private StatusCodeDTO statusCode;
}
