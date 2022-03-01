package com.example.demo.controller;
import com.example.demo.dto.*;
import com.example.demo.entity.Hotel;
import com.example.demo.service.FlightService;
import com.example.demo.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class AgencyController {


    @Autowired
    HotelService hotelService;
    @Autowired
    FlightService flightService;

    /**
     * Genera un Alta de hotel.
     * @param hotelDTO Objeto con los datos para realizar un alta de un nuevo vuelo.
     */
    @PostMapping("/api/v1/hotels/new")
    public ResponseEntity<HotelDTO> grabarHotel(@RequestBody @Valid HotelDTO hotelDTO){
        return new ResponseEntity<>(hotelService.grabarHotel(hotelDTO), HttpStatus.OK);
    }

    /**
     * Muestra todos los hoteles registrados en la BD.
     */
    @GetMapping("/api/v1/hotels")
    public ResponseEntity<List <Hotel>> mostrarHoteles()
    {
        return new ResponseEntity<>(hotelService.obtenerHoteles(), HttpStatus.OK);
    }

/**
     * Obtiene una lista de todos los hoteles disponibles, o una lista de los hoteles disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param destination El lugar destino.
     *//*

    @GetMapping("/api/v1/hotels")
    public ResponseEntity<List<HotelDTO>> obtenerHoteles(@RequestParam(required = false) @DateTimeFormat(pattern="dd/MM/yyyy") LocalDate dateFrom, @RequestParam(required = false) @DateTimeFormat(pattern="dd/MM/yyyy") LocalDate dateTo, @RequestParam(required = false) String destination){
        if (dateFrom == null && dateTo == null && destination == null)
            return new ResponseEntity<>(hotelService.obtenerHoteles(), HttpStatus.OK);
        return new ResponseEntity<>(hotelService.obtenerHotelesDisponibles(dateFrom, dateTo, destination), HttpStatus.OK);
    }

    */
/**
     * Realiza la reserva de un hotel en base a el objeto payloadHotelDTO.
     * @param payloadHotelDTO Objeto con los datos para realizar una reserva de hotel.
     *//*

    @PostMapping("/api/v1/booking")
    public ResponseEntity<ResponseHotelDTO> reservarHotel(@RequestBody @Valid PayloadHotelDTO payloadHotelDTO){
        return new ResponseEntity<>(hotelService.reservar(payloadHotelDTO), HttpStatus.OK);
    }

    */

    /**
     * Genera el alta de un nuevo vuelo.
     * @param flightDTO Objeto con los datos para realizar un alta de un nuevo vuelo.
     */
    @PostMapping("/api/v1/flights/new")
    public ResponseEntity<FlightDTO> grabarVuelo(@RequestBody @Valid FlightDTO flightDTO){
        return new ResponseEntity<>(flightService.grabarVuelo(flightDTO), HttpStatus.OK);
    }
/**
     * Obtiene una lista de todos los vuelos disponibles, o una lista de los vuelos disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param origin El lugar origen.
     * @param destination El lugar destino.
     *//*

    @GetMapping("/api/v1/flights")
    public ResponseEntity<List<FlightDTO>> obtenerVuelos(@RequestParam(required = false) @DateTimeFormat(pattern="dd/MM/yyyy") LocalDate dateFrom, @RequestParam(required = false) @DateTimeFormat(pattern="dd/MM/yyyy") LocalDate dateTo, @RequestParam(required = false) String origin, @RequestParam(required = false) String destination){
        if (dateFrom == null && dateTo == null && origin == null && destination == null)
            return new ResponseEntity<>(flightService.obtenerVuelos(), HttpStatus.OK);
        return new ResponseEntity<>(flightService.obtenerVuelosDisponibles(dateFrom,dateTo,origin,destination), HttpStatus.OK);
    }

    */
/**
     * Realiza la reserva de un vuelo en base a el objeto payloadFlightDTO.
     * @param payloadFlightDTO Objeto con los datos para realizar una reserva de vuelo.
     *//*

    @PostMapping("/api/v1/flight-reservation")
    public ResponseEntity<ResponseFlightDTO> reservarVuelo(@RequestBody PayloadFlightDTO payloadFlightDTO){
        return new ResponseEntity<>(flightService.reservar(payloadFlightDTO), HttpStatus.OK);
    }
*/
}