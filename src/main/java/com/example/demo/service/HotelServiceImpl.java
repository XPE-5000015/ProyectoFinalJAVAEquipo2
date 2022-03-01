package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Hotel;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.Hotels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

/*
    @Autowired
    Hotels hotels;

    */
/**
     * Obtiene una lista de todos los hoteles disponibles.
     *//*

    public List<HotelDTO> obtenerHoteles(){
        List<Hotel> hotelList = hotels.obtenerHoteles();
        if (hotelList.isEmpty())
            throw new NoContentException("No se encontraron hoteles.");
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            HotelDTO hotelDTO = transformarHotelAHotelDTO(hotel);
            hotelDTOList.add(hotelDTO);
        }
        return hotelDTOList;
    }

    */
/**
     * Obtiene una lista de los hoteles disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param destination El lugar destino.
     *//*

    public List<HotelDTO> obtenerHotelesDisponibles(LocalDate dateFrom, LocalDate dateTo, String destination){
        if (dateFrom.compareTo(dateTo) >= 0)
            throw new ConflictException("La fecha de salida debe ser mayor a la de entrada.");
        if (!hotels.obtenerDestinosValidos().contains(destination))
            throw new NoContentException("El destino elegido no existe.");
        List<Hotel> hotelList = hotels.obtenerHotelesDisponibles(dateFrom, dateTo, destination);
        if (hotelList.isEmpty())
            throw new NoContentException("No se encontraron hoteles disponibles para esta busqueda.");
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            HotelDTO hotelDTO = transformarHotelAHotelDTO(hotel);
            hotelDTOList.add(hotelDTO);
        }
        return hotelDTOList;
    }

    */
/**
     * Realiza la reserva de un hotel en base a el objeto payloadHotelDTO.
     * @param payloadHotelDTO Objeto con los datos para realizar una reserva de hotel.
     *//*

    public ResponseHotelDTO reservar(PayloadHotelDTO payloadHotelDTO){
        if (payloadHotelDTO.getBooking().getDateFrom().compareTo(payloadHotelDTO.getBooking().getDateTo()) >= 0)
            throw new ConflictException("La fecha de salida debe ser mayor a la de entrada.");
        if (!hotels.obtenerDestinosValidos().contains(payloadHotelDTO.getBooking().getDestination()))
            throw new NoContentException("El destino elegido no existe.");
        validarTipoHabitacionCantidadPersonas(payloadHotelDTO.getBooking());
        Hotel hotel = hotels.obtenerHotel(payloadHotelDTO.getBooking().getHotelCode());
*/
/*
        if (hotel.getReserved().equals("SI"))
            throw new ConflictException("Este hotel ya se encuentra reservado.");
*//*

        Period period = Period.between(payloadHotelDTO.getBooking().getDateFrom(), payloadHotelDTO.getBooking().getDateTo());

        Double interests = calculaIntereses(payloadHotelDTO.getBooking().getPaymentMethod());
        //double amount = Double.valueOf(hotel.getAmount()) * (period.getDays()-1);
        //Double total = amount * (1 + interests);

        ResponseHotelDTO responseHotelDTO = new ResponseHotelDTO();
        responseHotelDTO.setUserName(payloadHotelDTO.getUsername());
        //responseHotelDTO.setAmount(amount);
        responseHotelDTO.setInterest(interests*100);
        //responseHotelDTO.setTotal(total);
        responseHotelDTO.setBooking(transformarBookingPayloadABookingResponse(payloadHotelDTO.getBooking()));
        StatusCodeDTO statusCode = new StatusCodeDTO();
        statusCode.setCode(200);
        statusCode.setMessage("El proceso termino satisfactoriamente.");
        responseHotelDTO.setStatusCode(statusCode);

        //hotel.setReserved("SI");
        return responseHotelDTO;
    }

    */
/**
     * Realiza el calculo de los intereses en base al metodo de pago.
     * @param paymentMethodDTO Objeto con los datos del metodo de pago.
     *//*

    private Double calculaIntereses(PaymentMethodDTO paymentMethodDTO){
        double interests = 0.0;
        if (paymentMethodDTO.getType().equalsIgnoreCase("CREDIT") && paymentMethodDTO.getDues() > 1){
            if (paymentMethodDTO.getDues() > 18)
                throw new ConflictException("No se puede diferir la compra a mas de 18 meses.");
            if (paymentMethodDTO.getDues() <= 18)
                interests = 0.30;
            if (paymentMethodDTO.getDues() <= 12)
                interests = 0.20;
            if (paymentMethodDTO.getDues() <= 6)
                interests = 0.10;
            if (paymentMethodDTO.getDues() <= 3)
                interests = 0.05;
        }
        else {
            if (paymentMethodDTO.getDues() > 1)
                throw new ConflictException("Solo con tarjeta de credito se puede ingresar una cantidad de cuotas diferente a 1.");
        }
        return interests;
    }

    */
/**
     * Valida que el tipo de habitación seleccionada coincida con la cantidad de personas que se alojarán en ella.
     * @param bookingPayloadDTO Objeto con los datos del tipo de habitación, y cantidad de personas.
     *//*

    private void validarTipoHabitacionCantidadPersonas(BookingPayloadDTO bookingPayloadDTO){
        String message = "El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella.";
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("SINGLE") && bookingPayloadDTO.getPeopleAmount() != 1)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("DOUBLE") && bookingPayloadDTO.getPeopleAmount() != 2)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("TRIPLE") && bookingPayloadDTO.getPeopleAmount() != 3)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("MULTIPLE") && ((bookingPayloadDTO.getPeopleAmount() < 4) || (bookingPayloadDTO.getPeopleAmount() > 6)))
            throw new ConflictException(message);
    }

    */
/**
     * Transforma de un objeto BookingPayloadDTO a BookingResponseDTO.
     * @param bookingPayloadDTO Objeto con los datos necesarios para hacer la transformación.
     *//*

    private BookingResponseDTO transformarBookingPayloadABookingResponse(BookingPayloadDTO bookingPayloadDTO){
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setDateFrom(bookingPayloadDTO.getDateFrom());
        bookingResponseDTO.setDateTo(bookingPayloadDTO.getDateTo());
        bookingResponseDTO.setDestination(bookingPayloadDTO.getDestination());
        bookingResponseDTO.setHotelCode(bookingPayloadDTO.getHotelCode());
        bookingResponseDTO.setPeopleAmount(bookingPayloadDTO.getPeopleAmount());
        bookingResponseDTO.setRoomType(bookingPayloadDTO.getRoomType());
        bookingResponseDTO.setPeople(bookingPayloadDTO.getPeople());
        return bookingResponseDTO;
    }

    */
/**
     * Transforma de un objeto Hotel a HotelDTO.
     * @param hotel Objeto con los datos necesarios para hacer la transformación.
     */

    private HotelDTO transformarHotelAHotelDTO(Hotel hotel){
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelCode(hotel.getHotelCode());
        hotelDTO.setName(hotel.getName());

        hotelDTO.setPlace(hotel.getPlace());
        hotelDTO.setRoomType(hotel.getRoomType());
        hotelDTO.setRoomPrice(hotel.getRoomPrice());
        hotelDTO.setDisponibilityDateFrom(hotel.getDisponibilityDateFrom().toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate());
        hotelDTO.setDisponibilityDateTo(hotel.getDisponibilityDateTo().toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate());
        hotelDTO.setBooking(hotel.getIsBooking());

        return hotelDTO;
    }

}