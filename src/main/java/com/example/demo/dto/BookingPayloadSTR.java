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
    String dateFrom;
    String dateTo;
    String destination;
    String hotelCode;
    Integer peopleAmount;
    String roomType;
    @Valid
    List<PersonDTO> people;
    PaymentMethodDTO paymentMethod;

}
