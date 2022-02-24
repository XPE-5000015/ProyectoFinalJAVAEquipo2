package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHotelDTO {
    String userName;
    Double amount;
    Double interest;
    Double total;
    BookingResponseDTO booking;
    StatusCodeDTO statusCode;
}