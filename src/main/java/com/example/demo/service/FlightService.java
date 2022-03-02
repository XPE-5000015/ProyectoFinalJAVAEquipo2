package com.example.demo.service;

import com.example.demo.dto.FlightDTO;
import com.example.demo.dto.PayloadFlightDTO;
import com.example.demo.dto.ResponseFlightDTO;
import com.example.demo.dto.StatusCodeDTO;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    StatusCodeDTO deleteFlight(FlightDTO flightNumber);
    StatusCodeDTO editarVuelo(FlightDTO flightDTO);
    StatusCodeDTO grabarVuelo(FlightDTO flightDTO);
    List<FlightDTO> obtenerVuelos();
    List<FlightDTO> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
    StatusCodeDTO reservar(PayloadFlightDTO payloadFlightDTO);
}
