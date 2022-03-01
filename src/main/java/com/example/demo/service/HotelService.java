package com.example.demo.service;

import com.example.demo.dto.HotelDTO;
import com.example.demo.dto.PayloadHotelDTO;
import com.example.demo.dto.ResponseHotelDTO;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {
    HotelDTO grabarHotel(HotelDTO hotelDTO);
    List<HotelDTO> obtenerHoteles();
}
