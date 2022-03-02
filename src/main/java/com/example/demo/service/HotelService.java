package com.example.demo.service;

import com.example.demo.dto.HotelDTO;
import com.example.demo.dto.PayloadHotelDTO;
import com.example.demo.dto.ResponseHotelDTO;
import com.example.demo.dto.StatusCodeDTO;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {
    StatusCodeDTO grabarHotel(HotelDTO hotelDTO);
    List<HotelDTO> obtenerHoteles();
    StatusCodeDTO reservar(PayloadHotelDTO payloadHotelDTO);
}
