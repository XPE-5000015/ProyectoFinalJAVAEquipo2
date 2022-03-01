package com.example.demo.service;

import com.example.demo.dto.FlightDTO;
import com.example.demo.dto.PayloadFlightDTO;
import com.example.demo.dto.ResponseFlightDTO;
import com.example.demo.dto.StatusCodeDTO;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    StatusCodeDTO grabarVuelo(FlightDTO flightDTO);
}
