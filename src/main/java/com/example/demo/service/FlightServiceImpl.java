package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Flight;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.Flights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    Flights flights;

    /**
     * Obtiene una lista de todos los vuelos disponibles.
     */
    public List<FlightDTO> obtenerVuelos(){
        List<Flight> flightList = flights.obtenerVuelos();
        if (flightList.isEmpty())
            throw new NoContentException("No se encontraron vuelos.");
        List<FlightDTO> flightDTOList = new ArrayList<>();
        for (Flight flight : flightList) {
            FlightDTO flightDTO = transformarFlightAFlightDTO(flight);
            flightDTOList.add(flightDTO);
        }
        return flightDTOList;
    }

    /**
     * Obtiene una lista de los vuelos disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param origin El lugar origen.
     * @param destination El lugar destino.
     */
    public List<FlightDTO> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination){
        if (dateFrom.compareTo(dateTo) >= 0)
            throw new ConflictException("La fecha inicial no puede ser menor o igual a la fecha final.");
        if (!flights.obtenerOrigenesValidos().contains(origin))
            throw new NoContentException("El origen elegido no existe.");
        if (!flights.obtenerDestinosValidos().contains(destination))
            throw new NoContentException("El destino elegido no existe.");
        List<Flight> flightList = flights.obtenerVuelosDisponibles(dateFrom, dateTo, origin, destination);
        if (flightList.isEmpty())
            throw new NoContentException("No se encontraron vuelos disponibles para esta busqueda.");
        List<FlightDTO> flightDTOList = new ArrayList<>();
        for (Flight flight : flightList) {
            FlightDTO flightDTO = transformarFlightAFlightDTO(flight);
            flightDTOList.add(flightDTO);
        }
        return flightDTOList;
    }

    /**
     * Realiza la reserva de un vuelo en base a el objeto payloadFlightDTO.
     * @param payloadFlightDTO Objeto con los datos para realizar una reserva de vuelo.
     */
    public ResponseFlightDTO reservar(PayloadFlightDTO payloadFlightDTO){
        if (payloadFlightDTO.getFlightReservation().getDateFrom().compareTo(payloadFlightDTO.getFlightReservation().getDateTo()) >= 0)
            throw new ConflictException("La fecha inicial no puede ser menor o igual a la fecha final.");
        if (!flights.obtenerOrigenesValidos().contains(payloadFlightDTO.getFlightReservation().getOrigin()))
            throw new NoContentException("El origen elegido no existe.");
        if (!flights.obtenerDestinosValidos().contains(payloadFlightDTO.getFlightReservation().getDestination()))
            throw new NoContentException("El destino elegido no existe.");
        Flight flight = flights.obtenerVuelo(payloadFlightDTO.getFlightReservation().getFlightNumber());

        Double interests = calculaIntereses(payloadFlightDTO.getFlightReservation().getPaymentMethod());
        double amount = flight.getAmount() * payloadFlightDTO.getFlightReservation().getSeats();
        Double total = amount * (1 + interests);

        ResponseFlightDTO responseFlightDTO = new ResponseFlightDTO();
        responseFlightDTO.setUserName(payloadFlightDTO.getUserName());
        responseFlightDTO.setAmount(amount);
        responseFlightDTO.setInterest(4.5);
        responseFlightDTO.setTotal(total);
        responseFlightDTO.setFlightReservation(transformarFlightReservationPayloadDTOAFlightReservationResponseDTO(payloadFlightDTO.getFlightReservation()));
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(200);
        statusCodeDTO.setMessage("El proceso termino satisfactoriamente.");
        responseFlightDTO.setStatusCode(statusCodeDTO);
        return responseFlightDTO;
    }

    /**
     * Transforma de un objeto Flight a FlightDTO.
     * @param flight Objeto con los datos necesarios para hacer la transformaci??n.
     */
    private FlightDTO transformarFlightAFlightDTO(Flight flight){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightNumber(flight.getFlightNumber());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setSeatType(flight.getSeatType());
        flightDTO.setAmount(flight.getAmount());
        flightDTO.setDateFrom(flight.getDateFrom());
        flightDTO.setDateTo(flight.getDateTo());
        return flightDTO;
    }

    /**
     * Transforma de un objeto FlightReservationPayloadDTO a FlightReservationResponseDTO.
     * @param flightReservationPayloadDTO Objeto con los datos necesarios para hacer la transformaci??n.
     */
    private FlightReservationResponseDTO transformarFlightReservationPayloadDTOAFlightReservationResponseDTO(FlightReservationPayloadDTO flightReservationPayloadDTO){
        FlightReservationResponseDTO flightReservationResponseDTO = new FlightReservationResponseDTO();
        flightReservationResponseDTO.setDateFrom(flightReservationPayloadDTO.getDateFrom());
        flightReservationResponseDTO.setDateTo(flightReservationPayloadDTO.getDateTo());
        flightReservationResponseDTO.setOrigin(flightReservationPayloadDTO.getOrigin());
        flightReservationResponseDTO.setDestination(flightReservationPayloadDTO.getDestination());
        flightReservationResponseDTO.setFlightNumber(flightReservationPayloadDTO.getFlightNumber());
        flightReservationResponseDTO.setSeats(flightReservationPayloadDTO.getSeats());
        flightReservationResponseDTO.setSeatType(flightReservationPayloadDTO.getSeatType());
        flightReservationResponseDTO.setPeople(flightReservationPayloadDTO.getPeople());
        return flightReservationResponseDTO;
    }

    /**
     * Realiza el calculo de los intereses en base al metodo de pago.
     * @param paymentMethodDTO Objeto con los datos del metodo de pago.
     */
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

}
