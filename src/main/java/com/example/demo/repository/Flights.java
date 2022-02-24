package com.example.demo.repository;

import com.example.demo.entity.Flight;

import java.time.LocalDate;
import java.util.List;

public interface Flights {
    List<Flight> obtenerVuelos();
    List<String> obtenerOrigenesValidos();
    List<String> obtenerDestinosValidos();
    Flight obtenerVuelo(String flightNumber);
    List<Flight> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
}