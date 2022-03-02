package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    Flights flights;

    @Autowired
    Flight_reservations flight_reservations;

    @Autowired
    PaymentMethods paymentMethods;

    @Autowired
    People  people;

    @Autowired
    Reservation_people reservation_people;

    /**
     * Genera el alta de un nuevo vuelo.
     * @param flightDTO Objeto con los datos para realizar un alta de un nuevo vuelo.
     */
    public StatusCodeDTO grabarVuelo(FlightDTO flightDTO){
        if (flights.existsById(flightDTO.getFlightNumber()))
            throw new ConflictException("Este numero de vuelo ya existe.");
        Flight flight = transformarFlightDTOAFlight(flightDTO);
        flights.save(flight);
        return new StatusCodeDTO("Vuelo dado de alta correctamente");
    }

    /**
     * Edita un vuelo basado en su FlightNumber.
     * @param flightDTO Objeto con los datos para editar un vuelo.
     */
    public StatusCodeDTO editarVuelo(FlightDTO flightDTO){
        if (!flights.existsById(flightDTO.getFlightNumber()))
            throw new ConflictException("Este numero de vuelo no existe.");
        Flight flight = transformarFlightDTOAFlight(flightDTO);
        flight.setFlight_reservations(null);
        flights.save(flight);
        return new StatusCodeDTO("Vuelo modificado correctamente");
    }

    /**
     * Obtiene una lista de todos los vuelos disponibles.
     */

    public List<FlightDTO> obtenerVuelos(){
        List<Flight> flightList = flights.findAll();
        if (flightList.isEmpty())
            throw new NoContentException("No se encontraron vuelos.");
        List<FlightDTO> flightDTOList = new ArrayList<>();
        for (Flight flight : flightList) {
            FlightDTO flightDTO = transformarFlightAFlightDTO(flight);
            flightDTOList.add(flightDTO);
        }
        return flightDTOList;
    }
/**
     * Obtiene una lista de los vuelos disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param origin El lugar origen.
     * @param destination El lugar destino.
     *//*

    public List<FlightDTO> obtenerVuelosDisponibles(LocalDate dateFrom, LocalDate dateTo, String origin, String destination){
        if (dateFrom.compareTo(dateTo) >= 0)
            throw new ConflictException("La fecha inicial no puede ser menor o igual a la fecha final.");
        if (!flights.obtenerOrigenesValidos().contains(origin))
            throw new NoContentException("El origen elegido no existe.");
        if (!flights.obtenerDestinosValidos().contains(destination))
            throw new NoContentException("El destino elegido no existe.");
        List<Flight> flightList = flights.obtenerVuelosDisponibles(dateFrom, dateTo, origin, destination);
        if (flightList.isEmpty())
            throw new NoContentException("No se encontraron vuelos disponibles para esta busqueda.");
        List<FlightDTO> flightDTOList = new ArrayList<>();
        for (Flight flight : flightList) {
            FlightDTO flightDTO = transformarFlightAFlightDTO(flight);
            flightDTOList.add(flightDTO);
        }
        return flightDTOList;
    }

    */
/**
     * Realiza la reserva de un vuelo en base a el objeto payloadFlightDTO.
     * @param payloadFlightDTO Objeto con los datos para realizar una reserva de vuelo.
     */

    public StatusCodeDTO reservar(PayloadFlightDTO payloadFlightDTO){
        if (payloadFlightDTO.getFlightReservation().getGoingDate().compareTo(payloadFlightDTO.getFlightReservation().getReturnDate()) >= 0)
            throw new ConflictException("La fecha inicial no puede ser menor o igual a la fecha final.");

        List<Flight> flightsList = flights.findAll();
        List<String> origenes = flightsList.stream()
                .map(Flight::getOrigin)
                .distinct()
                .collect(Collectors.toList());
        List<String> destinos = flightsList.stream()
                .map(Flight::getDestination)
                .distinct()
                .collect(Collectors.toList());

        if (!origenes.contains(payloadFlightDTO.getFlightReservation().getOrigin()))
            throw new NoContentException("El origen elegido no existe.");
        if (!destinos.contains(payloadFlightDTO.getFlightReservation().getDestination()))
            throw new NoContentException("El destino elegido no existe.");
        Flight flight = flights.getById(payloadFlightDTO.getFlightReservation().getFlightNumber());

        Double interests = calculaIntereses(payloadFlightDTO.getFlightReservation().getPaymentMethod());
        double amount = flight.getFlightPrice() * payloadFlightDTO.getFlightReservation().getSeats();
        Double total = amount * (1 + interests);

        PaymentMethod paymentMethodSaved = savePaymentMethod(payloadFlightDTO.getFlightReservation().getPaymentMethod());

        Flight_reservation flight_reservationSaved = saveFlight_reservation(payloadFlightDTO, paymentMethodSaved.getPaymentMethod_id());

        List<PersonDTO> personDTOList = payloadFlightDTO.getFlightReservation().getPeople();
        List<Person> personList = new ArrayList<>();
        for (PersonDTO personDTO : personDTOList) {
            personList.add(transformarPersonDTOAPerson(personDTO));
        }

        savePeople(personList, flight_reservationSaved);

        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setMessage("Reserva de vuelo dada de alta correctamente.");
        return statusCodeDTO;
    }


    /**
     * Graba el metodo de pago.
     * @param paymentMethodDTO Objeto con los datos necesarios guardar el metodo de pago.
     */
    private PaymentMethod savePaymentMethod(PaymentMethodDTO paymentMethodDTO){
        PaymentMethod paymentMethodToSave = new PaymentMethod();
        paymentMethodToSave.setType(paymentMethodDTO.getType());
        paymentMethodToSave.setNumber(paymentMethodDTO.getNumber());
        paymentMethodToSave.setDues(paymentMethodDTO.getDues());
        return paymentMethods.save(paymentMethodToSave);
    }

    /**
     * Graba el metodo de pago.
     * @param personList Objeto con los datos necesarios guardar el metodo de pago.
     * @param flight_reservation Id de la reservación.
     */
    private void savePeople(List<Person> personList, Flight_reservation flight_reservation){
        for (Person person : personList) {
            if (!people.existsById(person.getDni()))
                people.save(person);
            Reservation_person reservation_person = new Reservation_person();
            reservation_person.setFlight_reservation(flight_reservation);
            reservation_person.setPerson(person);
            reservation_people.save(reservation_person);
        }
    }

    /**
     * Graba la reserva del vuelo.
     * @param payloadFlightDTO Objeto con los datos necesarios guardar la reserva del vuelo.
     * @param paymentMethod_id Id del método de pago.
     */
    private Flight_reservation saveFlight_reservation(PayloadFlightDTO payloadFlightDTO, Integer paymentMethod_id){
        Flight_reservation flight_reservation = new Flight_reservation();
        flight_reservation.setUserName(payloadFlightDTO.getUserName());
        flight_reservation.setGoingDate(java.util.Date.from(
                payloadFlightDTO.getFlightReservation().getGoingDate()
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        flight_reservation.setReturnDate(java.util.Date.from(
                payloadFlightDTO.getFlightReservation().getReturnDate()
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        flight_reservation.setOrigin(payloadFlightDTO.getFlightReservation().getOrigin());
        flight_reservation.setDestination(payloadFlightDTO.getFlightReservation().getDestination());
        flight_reservation.setFlightNumber(payloadFlightDTO.getFlightReservation().getFlightNumber());
        flight_reservation.setSeats(payloadFlightDTO.getFlightReservation().getSeats());
        flight_reservation.setSeatType(payloadFlightDTO.getFlightReservation().getSeatType());
        flight_reservation.setPaymentMethod_id(paymentMethod_id);
        return flight_reservations.save(flight_reservation);
    }

    /**
     * Transforma de un objeto Flight a FlightDTO.
     * @param flight Objeto con los datos necesarios para hacer la transformación.
     */
    private FlightDTO transformarFlightAFlightDTO(Flight flight){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightNumber(flight.getFlightNumber());
        flightDTO.setName(flight.getName());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setSeatType(flight.getSeatType());
        flightDTO.setFlightPrice(flight.getFlightPrice());
        flightDTO.setGoingDate(flight.getGoingDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        flightDTO.setReturnDate(flight.getReturnDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        return flightDTO;
    }

    /**
     * Transforma de un objeto PersonDTO a Person.
     * @param personDTO Objeto con los datos necesarios para hacer la transformación.
     */
    private Person transformarPersonDTOAPerson(PersonDTO personDTO){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Person person = new Person();
        person.setDni(personDTO.getDni());
        person.setName(personDTO.getName());
        person.setLastname(personDTO.getLastname());
        try {
            person.setBirthDate(formatter.parse(personDTO.getBirthDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        person.setMail(personDTO.getMail());
        return person;
    }

    /**
     * Transforma de un objeto FlightDTO a Flight.
     * @param flightDTO Objeto con los datos necesarios para hacer la transformación.
     */
    private Flight transformarFlightDTOAFlight(FlightDTO flightDTO){
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setName(flightDTO.getName());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setSeatType(flightDTO.getSeatType());
        flight.setFlightPrice(flightDTO.getFlightPrice());
        flight.setGoingDate(java.util.Date.from(flightDTO.getGoingDate().atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant()));
        flight.setReturnDate(java.util.Date.from(flightDTO.getReturnDate().atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant()));

        return flight;
    }


/**
     * Transforma de un objeto FlightReservationPayloadDTO a FlightReservationResponseDTO.
     * @param flightReservationPayloadDTO Objeto con los datos necesarios para hacer la transformación.
     *//*

    private FlightReservationResponseDTO transformarFlightReservationPayloadDTOAFlightReservationResponseDTO(FlightReservationPayloadDTO flightReservationPayloadDTO){
        FlightReservationResponseDTO flightReservationResponseDTO = new FlightReservationResponseDTO();
        flightReservationResponseDTO.setDateFrom(flightReservationPayloadDTO.getDateFrom());
        flightReservationResponseDTO.setDateTo(flightReservationPayloadDTO.getDateTo());
        flightReservationResponseDTO.setOrigin(flightReservationPayloadDTO.getOrigin());
        flightReservationResponseDTO.setDestination(flightReservationPayloadDTO.getDestination());
        flightReservationResponseDTO.setFlightNumber(flightReservationPayloadDTO.getFlightNumber());
        flightReservationResponseDTO.setSeats(flightReservationPayloadDTO.getSeats());
        flightReservationResponseDTO.setSeatType(flightReservationPayloadDTO.getSeatType());
        flightReservationResponseDTO.setPeople(flightReservationPayloadDTO.getPeople());
        return flightReservationResponseDTO;
    }

    */
/**
     * Realiza el calculo de los intereses en base al metodo de pago.
     * @param paymentMethodDTO Objeto con los datos del metodo de pago.
     */

    private Double calculaIntereses(PaymentMethodDTO paymentMethodDTO){
        double interests = 0.0;
        if (paymentMethodDTO.getType().equalsIgnoreCase("CREDIT") && paymentMethodDTO.getDues() > 1){
            if (paymentMethodDTO.getDues() > 18)
                throw new ConflictException("No se puede diferir la compra a mas de 18 meses.");
            if (paymentMethodDTO.getDues() <= 18)
                interests = 0.30;
            if (paymentMethodDTO.getDues() <= 12)
                interests = 0.20;
            if (paymentMethodDTO.getDues() <= 6)
                interests = 0.10;
            if (paymentMethodDTO.getDues() <= 3)
                interests = 0.05;
        }
        else {
            if (paymentMethodDTO.getDues() > 1)
                throw new ConflictException("Solo con tarjeta de credito se puede ingresar una cantidad de cuotas diferente a 1.");
        }
        return interests;
    }
}
