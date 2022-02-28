package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingPayloadSTR {
    private String dateFrom;
    private String dateTo;
    private String destination;
    private String hotelCode;
    private Integer peopleAmount;
    private String roomType;
    @Valid
    private List<PersonDTO> people;
    private PaymentMethodDTO paymentMethod;
}
