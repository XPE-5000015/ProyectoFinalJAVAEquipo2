package com.example.demo.repository;

import com.example.demo.entity.Flight;
import com.example.demo.exceptions.NoContentException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FlightsImpl implements Flights {

    List<Flight> flights;
    public FlightsImpl(){
        flights = loadDataBase();
    }

    /**
     * Obtiene una lista de todos los vuelos disponibles.
     */
    @Override
    public List<Flight> obtenerVuelos(){
        return flights;
    }

    /**
     * Obtiene una lista de todos los origenes validos.
     */
    public List<String> obtenerOrigenesValidos(){
        return flights.stream()
                .map(Flight::getOrigin)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los destinos validos.
     */
    public List<String> obtenerDestinosValidos(){
        return flights.stream()
                .map(Flight::getDestination)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un vuelo en base a un numero de vuelo.
     * @param flightNumber numero de vuelo.
     */
    public Flight obtenerVuelo(String flightNumber){
        return flights.stream().filter(x -> x.getFlightNumber().equals(flightNumber)).findFirst().orElseThrow(() -> new NoContentException("No se encontró el codigo del vuelo."));
    }

    /**
     * Obtiene una lista de los vuelos disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param origin El lugar origen.
     * @param destination El lugar destino.
     */
    public List<Flight> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination){
        return flights.stream()
                .filter(x -> (x.getDateFrom().compareTo(dateFrom) <= 0)
                        && (x.getDateTo().compareTo(dateTo) >= 0)
                        && x.getOrigin().equals(origin)
                        && x.getDestination().equals(destination))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los vuelos leyendo el archivo vuelos.json.
     */
    private List<Flight> loadDataBase(){
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:vuelos.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Flight>> typeRef = new TypeReference<>() {};
        List<Flight> flightsList = null;
        try {
            flightsList = objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flightsList;
    }
}