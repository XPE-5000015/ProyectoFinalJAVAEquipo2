package com.example.demo.repository;

import com.example.demo.entity.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface Hotels {
    List<Hotel> obtenerHoteles();
    List<String> obtenerDestinosValidos();
    Hotel obtenerHotel(String hotelCode);
    List<Hotel> obtenerHotelesDisponibles(LocalDate dateFrom, LocalDate dateTo, String destination);
}