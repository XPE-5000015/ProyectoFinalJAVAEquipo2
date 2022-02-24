package com.example.demo.repository;

import com.example.demo.entity.Hotel;
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
public class HotelsImpl implements Hotels {

    List<Hotel> hotels;
    public HotelsImpl(){
        hotels = loadDatabase();
    }

    /**
     * Obtiene una lista de todos los hoteles disponibles.
     */
    @Override
    public List<Hotel> obtenerHoteles() {
        return hotels;
    }

    /**
     * Obtiene una lista de todos los destinos validos.
     */
    public List<String> obtenerDestinosValidos(){
        return hotels.stream()
                .map(Hotel::getDestination)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un hotel en base a un codigo de hotel.
     * @param hotelCode codigo del hotel.
     */
    public Hotel obtenerHotel(String hotelCode){
        return hotels.stream().filter(x -> x.getHotelCode().equals(hotelCode)).findFirst().orElseThrow(() -> new NoContentException("No se encontró el codigo del hotel."));
    }

    /**
     * Obtiene una lista de los hoteles disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param destination El lugar destino.
     */
    public List<Hotel> obtenerHotelesDisponibles(LocalDate dateFrom, LocalDate dateTo, String destination){
        return hotels.stream()
                .filter(x -> (x.getDateFrom().compareTo(dateFrom) <= 0)
                        && (x.getDateTo().compareTo(dateTo) >= 0)
                        && x.getDestination().equals(destination)
                        && x.getReserved().equals("NO"))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los hoteles leyendo el archivo hoteles.json.
     */
    private List<Hotel> loadDatabase(){
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:hoteles.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Hotel>> typeRef = new TypeReference<>() {};
        List<Hotel> hotelsList = null;
        try {
            hotelsList = objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hotelsList;
    }
}