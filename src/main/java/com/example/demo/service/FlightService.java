package com.example.demo.service;

import com.example.demo.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    StatusCodeDTO editarVuelo(FlightDTO flightDTO);
    StatusCodeDTO grabarVuelo(FlightDTO flightDTO);
    List<FlightDTO> obtenerVuelos();
    List<FlightDTO> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
    StatusCodeDTO reservar(PayloadFlightDTO payloadFlightDTO);
    List<FlightReservationPayloadDTO> obtenerReservaVuelos();
}
