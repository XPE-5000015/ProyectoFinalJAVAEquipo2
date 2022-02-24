package com.example.demo.repository;

import com.example.demo.entity.Flight;
import com.example.demo.exceptions.NoContentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FlightsImplTest {

    Flights flights = new FlightsImpl();

    @Test
    public void obtenerVuelosTest(){
        //Arrange
        //Act
        List<Flight> result = flights.obtenerVuelos();
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerOrigenesValidosTest(){
        //Arrange
        //Act
        List<String> result = flights.obtenerOrigenesValidos();
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerDestinosValidosTest(){
        //Arrange
        //Act
        List<String> result = flights.obtenerDestinosValidos();
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerVueloTestFound(){
        //Arrange
        String flightNumber = "PIBA-1420";
        //Act
        Flight result = flights.obtenerVuelo(flightNumber);
        //Assert
        assertEquals("PIBA-1420",result.getFlightNumber());
        assertEquals("Puerto Iguazú",result.getOrigin());
        assertEquals("Bogotá",result.getDestination());
    }

    @Test
    public void obtenerVueloTestNotFound(){
        //Arrange
        String flightNumber = "PIBA-20";
        //Act
        NoContentException thrown = assertThrows(NoContentException.class, () -> flights.obtenerVuelo(flightNumber));
        //Assert
        assertEquals("No se encontró el codigo del vuelo.",thrown.getMessage());
    }

    @Test
    public void obtenerVuelosDisponiblesTestPopulate(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022,2,11);
        LocalDate dateTo = LocalDate.of(2022,2,18);
        String origin = "Puerto Iguazú";
        String destination = "Bogotá";
        //Act
        List<Flight> result = flights.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination);
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerVuelosDisponiblesTestEmpty(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2021,2,11);
        LocalDate dateTo = LocalDate.of(2021,2,18);
        String origin = "Puerto Iguazú";
        String destination = "Bogotá";
        //Act
        List<Flight> result = flights.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination);
        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
