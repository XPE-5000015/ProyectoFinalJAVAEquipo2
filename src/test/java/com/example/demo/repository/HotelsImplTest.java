package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import com.example.demo.exceptions.NoContentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HotelsImplTest {

    Hotels hotels = new HotelsImpl();

    @Test
    public void obtenerHotelesTest(){
        //Arrange
        //Act
        List<Hotel> result = hotels.obtenerHoteles();
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerDestinosValidosTest(){
        //Arrange
        //Act
        List<String> result = hotels.obtenerDestinosValidos();
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerHotelTestFound(){
        //Arrange
        String hotelCode = "HB-0001";
        //Act
        Hotel result = hotels.obtenerHotel(hotelCode);
        //Assert
        assertEquals("HB-0001",result.getHotelCode());
        assertEquals("Hotel Bristol",result.getName());
    }

    @Test
    public void obtenerHotelTestNotFound(){
        //Arrange
        String hotelCode = "HB-01";
        //Act
        NoContentException thrown = assertThrows(NoContentException.class, () -> hotels.obtenerHotel(hotelCode));
        //Assert
        assertEquals("No se encontró el codigo del hotel.",thrown.getMessage());
    }

    @Test
    public void obtenerHotelesDisponiblesPopulate(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 12);
        LocalDate dateTo = LocalDate.of(2022, 3, 19);
        String destination = "Buenos Aires";
        //Act
        List<Hotel> result = hotels.obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        //Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void obtenerHotelesDisponiblesEmpty(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2021, 2, 12);
        LocalDate dateTo = LocalDate.of(2021, 3, 19);
        String destination = "Buenos Aires";
        //Act
        List<Hotel> result = hotels.obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
