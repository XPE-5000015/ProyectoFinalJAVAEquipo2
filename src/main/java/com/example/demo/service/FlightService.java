package com.example.demo.service;

import com.example.demo.dto.FlightDTO;
import com.example.demo.dto.PayloadFlightDTO;
import com.example.demo.dto.ResponseFlightDTO;
import com.example.demo.entity.Flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    FlightDTO grabarVuelo(FlightDTO flightDTO);
    public List <Flight> todosVuelos();
}
