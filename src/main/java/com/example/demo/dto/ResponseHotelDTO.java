package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHotelDTO {
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private BookingResponseDTO booking;
    private StatusCodeDTO statusCode;
}