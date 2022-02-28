package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Flight;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.Flights;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImplTest {
/*

    @Mock
    Flights flights;

    @InjectMocks
    FlightServiceImpl flightService;

    @Test
    public void obtenerVuelosTest(){
        //Arrange
        //Act
        when(flights.obtenerVuelos()).thenReturn(flightList());
        List<FlightDTO> flightDTOList = flightService.obtenerVuelos();
        //Assert
        assertNotNull(flightDTOList);
        assertTrue(flightDTOList.size() > 0);
    }

    @Test
    public void obtenerVuelosTestEmptyList(){
        //Arrange
        List<Flight> flightList = new ArrayList<>();
        //Act
        when(flights.obtenerVuelos()).thenReturn(flightList);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.obtenerVuelos());
        //Assert
        verify(flights, atLeast(1)).obtenerVuelos();
        assertEquals("No se encontraron vuelos.",thrown.getMessage());
    }

    @Test
    public void obtenerVuelosDisponiblesTestFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 11);
        LocalDate dateTo = LocalDate.of(2022, 2, 18);
        String origin = "Puerto Iguazú";
        String destination = "Bogotá";
        List<String> origins = new ArrayList<>();
        origins.add(origin);
        List<String> destinations = new ArrayList<>();
        destinations.add(destination);
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        when(flights.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination)).thenReturn(flightList());
        List<FlightDTO> result = flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination);
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        verify(flights, atLeast(1)).obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerVuelosDisponiblesTestNotFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2021, 2, 11);
        LocalDate dateTo = LocalDate.of(2021, 2, 18);
        String origin = "Puerto Iguazú";
        String destination = "Bogotá";
        List<Flight> flightList = new ArrayList<>();
        List<String> origins = new ArrayList<>();
        origins.add(origin);
        List<String> destinations = new ArrayList<>();
        destinations.add(destination);
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        when(flights.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination)).thenReturn(flightList);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        verify(flights, atLeast(1)).obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination);
        assertEquals("No se encontraron vuelos disponibles para esta busqueda.",thrown.getMessage());
    }

    @Test
    public void obtenerVuelosDisponiblesTestDestinationNotFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 11);
        LocalDate dateTo = LocalDate.of(2022, 2, 18);
        String origin = "Puerto Iguazú";
        String destination = "Prueba";
        List<String> origins = new ArrayList<>();
        origins.add(origin);
        List<String> destinations = new ArrayList<>();
        destinations.add("Bogotá");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        assertEquals("El destino elegido no existe.",thrown.getMessage());
    }

    @Test
    public void obtenerVuelosDisponiblesTestOriginNotFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 11);
        LocalDate dateTo = LocalDate.of(2022, 2, 18);
        String origin = "Prueba";
        String destination = "Bogotá";
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        assertEquals("El origen elegido no existe.",thrown.getMessage());
    }

    @Test
    public void obtenerVuelosDisponiblesTestDateConflict(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 21);
        LocalDate dateTo = LocalDate.of(2022, 2, 18);
        String origin = "Puerto Iguazú";
        String destination = "Bogotá";
        //Act
        ConflictException thrown = assertThrows(ConflictException.class, () -> flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination));
        //Assert
        assertEquals("La fecha inicial no puede ser menor o igual a la fecha final.",thrown.getMessage());
    }

    @Test
    public void reservarTestSuccessful(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        List<String> destinations = new ArrayList<>();
        destinations.add("Bogotá");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        when(flights.obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber())).thenReturn(flight43200());
        ResponseFlightDTO result = flightService.reservar(payloadFlightDTO);
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        verify(flights, atLeast(1)).obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber());
        assertEquals(200,result.getStatusCode().getCode());
        assertEquals("El proceso termino satisfactoriamente.",result.getStatusCode().getMessage());
    }

    @Test
    public void reservarTestPaymentMethodConflict(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        payloadFlightDTO.getFlightReservation().getPaymentMethod().setType("DEBIT");
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        List<String> destinations = new ArrayList<>();
        destinations.add("Bogotá");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        when(flights.obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber())).thenReturn(flight43200());
        ConflictException thrown = assertThrows(ConflictException.class, () -> flightService.reservar(payloadFlightDTO));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        verify(flights, atLeast(1)).obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber());
        assertEquals("Solo con tarjeta de credito se puede ingresar una cantidad de cuotas diferente a 1.",thrown.getMessage());
    }

    @Test
    public void reservarTestDuesConflict(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        payloadFlightDTO.getFlightReservation().getPaymentMethod().setDues(19);
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        List<String> destinations = new ArrayList<>();
        destinations.add("Bogotá");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        when(flights.obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber())).thenReturn(flight43200());
        ConflictException thrown = assertThrows(ConflictException.class, () -> flightService.reservar(payloadFlightDTO));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        verify(flights, atLeast(1)).obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber());
        assertEquals("No se puede diferir la compra a mas de 18 meses.",thrown.getMessage());
    }

    @Test
    public void reservarTestDestinationNotFound(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        payloadFlightDTO.getFlightReservation().setDestination("Prueba");
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        List<String> destinations = new ArrayList<>();
        destinations.add("Bogotá");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        when(flights.obtenerDestinosValidos()).thenReturn(destinations);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.reservar(payloadFlightDTO));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        verify(flights, atLeast(1)).obtenerDestinosValidos();
        assertEquals("El destino elegido no existe.",thrown.getMessage());
    }

    @Test
    public void reservarTestOriginNotFound(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        payloadFlightDTO.getFlightReservation().setOrigin("Prueba");
        List<String> origins = new ArrayList<>();
        origins.add("Puerto Iguazú");
        //Act
        when(flights.obtenerOrigenesValidos()).thenReturn(origins);
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.reservar(payloadFlightDTO));
        //Assert
        verify(flights, atLeast(1)).obtenerOrigenesValidos();
        assertEquals("El origen elegido no existe.",thrown.getMessage());
    }

    @Test
    public void reservarTestDateConflict(){
        //Arrange
        PayloadFlightDTO payloadFlightDTO = payloadFlightDTO();
        payloadFlightDTO.getFlightReservation().setOrigin("Prueba");
        //Act
        NoContentException thrown = assertThrows(NoContentException.class, () -> flightService.reservar(payloadFlightDTO));
        //Assert
        assertEquals("El origen elegido no existe.",thrown.getMessage());
    }

    private Flight flight43200(){
        return new Flight("PIBA-1420"
        ,"Puerto Iguazú"
        ,"Bogotá"
        ,"Business"
        ,43200
        , LocalDate.of(2022,2,10)
        ,LocalDate.of(2022,2,20));
    }

    private Flight flight25735(){
        return new Flight("PIBA-1420"
                ,"Puerto Iguazú"
                ,"Bogotá"
                ,"Business"
                ,25735
                , LocalDate.of(2022,2,10)
                ,LocalDate.of(2022,2,21));
    }

    private List<Flight> flightList(){
        List<Flight> flightList = new ArrayList<>();
        flightList.add(flight43200());
        flightList.add(flight25735());
        return flightList;
    }

    private PayloadFlightDTO payloadFlightDTO(){

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT","1234-1234-1234-1234",6);

        PersonDTO personDTO1 = new PersonDTO("12345678","Pepito","Gomez","10/11/1982","arjonamiguel@gmail.com");
        PersonDTO personDTO2 = new PersonDTO("12345678","Fulanito","Gomez","10/11/1983","arjonamiguel2@gmail.com");
        List<PersonDTO> people = new ArrayList<>();
        people.add(personDTO1);
        people.add(personDTO2);

        FlightReservationPayloadDTO flightReservationPayloadDTO = new FlightReservationPayloadDTO();
        flightReservationPayloadDTO.setDateFrom(LocalDate.of(2022,2,12));
        flightReservationPayloadDTO.setDateTo(LocalDate.of(2022,2,19));
        flightReservationPayloadDTO.setOrigin("Puerto Iguazú");
        flightReservationPayloadDTO.setDestination("Bogotá");
        flightReservationPayloadDTO.setFlightNumber("PIBA-1420");
        flightReservationPayloadDTO.setSeats(2);
        flightReservationPayloadDTO.setSeatType("ECONOMY");
        flightReservationPayloadDTO.setPeople(people);
        flightReservationPayloadDTO.setPaymentMethod(paymentMethodDTO);

        PayloadFlightDTO payloadFlightDTO = new PayloadFlightDTO();
        payloadFlightDTO.setUserName("arjonamiguel@gmail.com");
        payloadFlightDTO.setFlightReservation(flightReservationPayloadDTO);

        return payloadFlightDTO;
    }
*/
}
