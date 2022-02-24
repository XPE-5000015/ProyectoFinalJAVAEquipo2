package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Hotel;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.Hotels;
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
public class HotelServiceImplTest {

    @Mock
    Hotels hotels;

    @InjectMocks
    HotelServiceImpl hotelService;

    @Test
    public void obtenerHotelesTest(){
        //Arrange
        //Act
        when(hotels.obtenerHoteles()).thenReturn(hotelList());
        List<HotelDTO> hotelDTOList = hotelService.obtenerHoteles();
        //Assert
         assertNotNull(hotelDTOList);
        assertTrue(hotelDTOList.size() > 0);
    }

    @Test
    public void obtenerHotelesTestEmptyList(){
        //Arrange
        List<Hotel> hotelList = new ArrayList<>();
        //Act
        when(hotels.obtenerHoteles()).thenReturn(hotelList);
        NoContentException thrown = assertThrows(NoContentException.class, () -> hotelService.obtenerHoteles());
        //Assert
        verify(hotels, atLeast(1)).obtenerHoteles();
        assertEquals("No se encontraron hoteles.",thrown.getMessage());
    }

    @Test
    public void obtenerHotelesDisponiblesTestFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 12);
        LocalDate dateTo = LocalDate.of(2022, 3, 19);
        String destination = "Buenos Aires";
        List<String> destinations = new ArrayList<>();
        destinations.add(destination);
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotelesDisponibles(dateFrom, dateTo, destination)).thenReturn(hotelList());
        List<HotelDTO> hotelDTOList = hotelService.obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        //Assert
        verify(hotels, atLeast(1)).obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        assertNotNull(hotelDTOList);
        assertTrue(hotelDTOList.size() > 0);
    }

    @Test
    public void obtenerHotelesDisponiblesTestNotFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2021, 2, 12);
        LocalDate dateTo = LocalDate.of(2021, 3, 19);
        String destination = "Buenos Aires";
        List<String> destinations = new ArrayList<>();
        destinations.add(destination);
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotelesDisponibles(dateFrom, dateTo, destination)).thenReturn(new ArrayList<>());
        NoContentException thrown = assertThrows(NoContentException.class, () -> hotelService.obtenerHotelesDisponibles(dateFrom, dateTo, destination));
        //Assert
        verify(hotels, atLeast(1)).obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        assertEquals("No se encontraron hoteles disponibles para esta busqueda.",thrown.getMessage());
    }

    @Test
    public void obtenerHotelesDisponiblesTestDateConflict(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 3, 12);
        LocalDate dateTo = LocalDate.of(2022, 2, 19);
        String destination = "Buenos Aires";
        //Act
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.obtenerHotelesDisponibles(dateFrom, dateTo, destination));
        //Assert
        assertEquals("La fecha de salida debe ser mayor a la de entrada.",thrown.getMessage());
    }

    @Test
    public void obtenerHotelesDisponiblesTestDestinationNotFound(){
        //Arrange
        LocalDate dateFrom = LocalDate.of(2022, 2, 12);
        LocalDate dateTo = LocalDate.of(2022, 3, 19);
        String destination = "Prueba";
        List<String> destinations = new ArrayList<>();
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        NoContentException thrown = assertThrows(NoContentException.class, () -> hotelService.obtenerHotelesDisponibles(dateFrom, dateTo, destination));
        //Assert
        assertEquals("El destino elegido no existe.",thrown.getMessage());
    }

    @Test
    public void reservarTestSuccessful(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotel(payloadHotelDTO.getBooking().getHotelCode())).thenReturn(hotelBH0002());
        ResponseHotelDTO result = hotelService.reservar(payloadHotelDTO);
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        verify(hotels, atLeast(1)).obtenerHotel(payloadHotelDTO.getBooking().getHotelCode());
        assertEquals(200,result.getStatusCode().getCode());
        assertEquals("El proceso termino satisfactoriamente.",result.getStatusCode().getMessage());
    }

    @Test
    public void reservarTestDateConflict(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        payloadHotelDTO.getBooking().setDateFrom(LocalDate.of(2022, 5, 20));
        //Act
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        assertEquals("La fecha de salida debe ser mayor a la de entrada.",thrown.getMessage());
    }

    @Test
    public void reservarTestDestinationNotFound(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        payloadHotelDTO.getBooking().setDestination("Prueba");
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        NoContentException thrown = assertThrows(NoContentException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        assertEquals("El destino elegido no existe.",thrown.getMessage());
    }

    @Test
    public void reservarTestTypeRoomPeopleConflict(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        payloadHotelDTO.getBooking().setPeopleAmount(3);
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        assertEquals("El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella.",thrown.getMessage());
    }

    @Test
    public void reservarTestReservedConflict(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        Hotel hotel = hotelBH0002();
        hotel.setReserved("SI");
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotel(payloadHotelDTO.getBooking().getHotelCode())).thenReturn(hotel);
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        verify(hotels, atLeast(1)).obtenerHotel(payloadHotelDTO.getBooking().getHotelCode());
        assertEquals("Este hotel ya se encuentra reservado.",thrown.getMessage());
    }

    @Test
    public void reservarTestPaymentMethodConflict(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        Hotel hotel = hotelBH0002();
        payloadHotelDTO.getBooking().getPaymentMethod().setType("DEBIT");
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotel(payloadHotelDTO.getBooking().getHotelCode())).thenReturn(hotel);
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        verify(hotels, atLeast(1)).obtenerHotel(payloadHotelDTO.getBooking().getHotelCode());
        assertEquals("Solo con tarjeta de credito se puede ingresar una cantidad de cuotas diferente a 1.",thrown.getMessage());
    }

    @Test
    public void reservarTestDuesConflict(){
        //Arrange
        PayloadHotelDTO payloadHotelDTO = payloadHotelDTO();
        List<String> destinations = new ArrayList<>();
        destinations.add("Buenos Aires");
        Hotel hotel = hotelBH0002();
        payloadHotelDTO.getBooking().getPaymentMethod().setDues(19);
        //Act
        when(hotels.obtenerDestinosValidos()).thenReturn(destinations);
        when(hotels.obtenerHotel(payloadHotelDTO.getBooking().getHotelCode())).thenReturn(hotel);
        ConflictException thrown = assertThrows(ConflictException.class, () -> hotelService.reservar(payloadHotelDTO));
        //Assert
        verify(hotels, atLeast(1)).obtenerDestinosValidos();
        verify(hotels, atLeast(1)).obtenerHotel(payloadHotelDTO.getBooking().getHotelCode());
        assertEquals("No se puede diferir la compra a mas de 18 meses.",thrown.getMessage());
    }

    private Hotel hotelHB0001(){
        return new Hotel("HB-0001"
                ,"Hotel Bristol"
                ,"Buenos Aires"
                ,"Single"
                ,5435
                ,LocalDate.of(2022, 2, 10)
                ,LocalDate.of(2022, 3, 19)
                ,"NO");
    }

    private Hotel hotelBH0002(){
        return new Hotel("BH-0002"
                ,"Hotel Bristol 2"
                ,"Buenos Aires"
                ,"Doble"
                ,7200
                ,LocalDate.of(2022, 2, 12)
                ,LocalDate.of(2022, 4, 17)
                ,"NO");
    }

    private List<Hotel> hotelList(){
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotelHB0001());
        hotelList.add(hotelBH0002());
        return hotelList;
    }

    private PayloadHotelDTO payloadHotelDTO(){

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT","1234-1234-1234-1234",6);

        PersonDTO personDTO1 = new PersonDTO("12345678","Pepito","Gomez","10/11/1982","arjonamiguel@gmail.com");
        PersonDTO personDTO2 = new PersonDTO("12345678","Fulanito","Gomez","10/11/1983","arjonamiguel2@gmail.com");
        List<PersonDTO> people = new ArrayList<>();
        people.add(personDTO1);
        people.add(personDTO2);

        BookingPayloadDTO bookingPayloadDTO = new BookingPayloadDTO();
        bookingPayloadDTO.  setDateFrom(LocalDate.of(2022, 2, 20));
        bookingPayloadDTO.setDateTo(LocalDate.of(2022, 4, 15));
        bookingPayloadDTO.setDestination("Buenos Aires");
        bookingPayloadDTO.setHotelCode("BH-0002");
        bookingPayloadDTO.setPeopleAmount(2);
        bookingPayloadDTO.setRoomType("DOUBLE");
        bookingPayloadDTO.setPeople(people);
        bookingPayloadDTO.setPaymentMethod(paymentMethodDTO);

        PayloadHotelDTO payloadHotelDTO = new PayloadHotelDTO();
        payloadHotelDTO.setUsername("arjonamiguel@gmail.com");
        payloadHotelDTO.setBooking(bookingPayloadDTO);

        return payloadHotelDTO;
    }
}
