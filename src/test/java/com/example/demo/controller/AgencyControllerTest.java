package com.example.demo.controller;

import com.example.demo.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AgencyControllerTest {

/*
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void obtenerHotelesTest() throws Exception{
        //Arrange
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void obtenerHotelesTestWithParameters() throws Exception{
        //Arrange
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels?dateFrom=12/02/2022&dateTo=19/03/2022&destination=Buenos Aires"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void reservarHotelTest() throws Exception{
        //Arrange
        PayloadHotelSTR payloadHotelSTR = payloadHotelSTR();

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadJson = writer.writeValueAsString(payloadHotelSTR);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/booking")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void obtenerVuelosTest() throws Exception{
        //Arrange
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void obtenerVuelosTestWithParameters() throws Exception{
        //Arrange
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights?dateFrom=11/02/2022&dateTo=18/02/2022&origin=Puerto Iguazú&destination=Bogotá"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void reservarVueloTest() throws Exception{
        //Arrange
        PayloadFlightSTR payloadFlightSTR = payloadFlightSTR();

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer().withDefaultPrettyPrinter();

        String payloadJson = writer.writeValueAsString(payloadFlightSTR);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private PayloadHotelSTR payloadHotelSTR(){

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT","1234-1234-1234-1234",6);

        PersonDTO personDTO1 = new PersonDTO("12345678","Pepito","Gomez","10/11/1982","arjonamiguel@gmail.com");
        PersonDTO personDTO2 = new PersonDTO("12345678","Fulanito","Gomez","10/11/1983","arjonamiguel2@gmail.com");
        List<PersonDTO> people = new ArrayList<>();
        people.add(personDTO1);
        people.add(personDTO2);

        BookingPayloadSTR bookingPayloadSTR = new BookingPayloadSTR();
        bookingPayloadSTR.setDateFrom("20/02/2022");
        bookingPayloadSTR.setDateTo("15/04/2022");
        bookingPayloadSTR.setDestination("Buenos Aires");
        bookingPayloadSTR.setHotelCode("BH-0002");
        bookingPayloadSTR.setPeopleAmount(2);
        bookingPayloadSTR.setRoomType("DOUBLE");
        bookingPayloadSTR.setPeople(people);
        bookingPayloadSTR.setPaymentMethod(paymentMethodDTO);

        PayloadHotelSTR payloadHotelSTR = new PayloadHotelSTR();
        payloadHotelSTR.setUsername("arjonamiguel@gmail.com");
        payloadHotelSTR.setBooking(bookingPayloadSTR);

        return payloadHotelSTR;
    }

    private PayloadFlightSTR payloadFlightSTR(){

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT","1234-1234-1234-1234",6);

        PersonDTO personDTO1 = new PersonDTO("12345678","Pepito","Gomez","10/11/1982","arjonamiguel@gmail.com");
        PersonDTO personDTO2 = new PersonDTO("12345678","Fulanito","Gomez","10/11/1983","arjonamiguel2@gmail.com");
        List<PersonDTO> people = new ArrayList<>();
        people.add(personDTO1);
        people.add(personDTO2);

        FlightReservationPayloadSTR flightReservationPayloadSTR = new FlightReservationPayloadSTR();
        flightReservationPayloadSTR.setDateFrom("12/02/2022");
        flightReservationPayloadSTR.setDateTo("19/02/2022");
        flightReservationPayloadSTR.setOrigin("Puerto Iguazú");
        flightReservationPayloadSTR.setDestination("Bogotá");
        flightReservationPayloadSTR.setFlightNumber("PIBA-1420");
        flightReservationPayloadSTR.setSeats(2);
        flightReservationPayloadSTR.setSeatType("ECONOMY");
        flightReservationPayloadSTR.setPeople(people);
        flightReservationPayloadSTR.setPaymentMethod(paymentMethodDTO);

        PayloadFlightSTR payloadFlightSTR = new PayloadFlightSTR();
        payloadFlightSTR.setUserName("arjonamiguel@gmail.com");
        payloadFlightSTR.setFlightReservation(flightReservationPayloadSTR);

        return payloadFlightSTR;
    }
*/
}
