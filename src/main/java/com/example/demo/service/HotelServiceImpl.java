package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NoContentException;
import com.example.demo.repository.Booking_people;
import com.example.demo.repository.Hotels;
import com.example.demo.repository.PaymentMethods;
import com.example.demo.repository.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {


    @Autowired
    Hotels hotels;

    @Autowired
    PaymentMethods paymentMethods;

    @Autowired
    People people;

    @Autowired
    Booking_people booking_people;

    /**
     * Genera el alta de un nuevo hotel.
     * @param hotelDTO Objeto con los datos para realizar un alta de un nuevo hotel.
     */
    public StatusCodeDTO grabarHotel(HotelDTO hotelDTO)
    {
        if(hotels.existsById(hotelDTO.getHotelCode()))
            throw new ConflictException("Este Codigo de hotel ya existe.");
        Hotel hotel = transformarHotelDTOAHotel(hotelDTO);
        hotels.save(hotel);
        return new StatusCodeDTO("Hotel dado de alta");
    }

    /**
     * Edita un Hotel basado en su HotelCode.
     * @param hotelDTO Objeto con los datos para editar un Hotel.
     */
    public StatusCodeDTO editarHotel(HotelDTO hotelDTO){
        if (!hotels.existsById(hotelDTO.getHotelCode()))
            throw new ConflictException("Este numero de Hotel no existe.");
        Hotel hotel = transformarHotelDTOAHotel(hotelDTO);
        hotel.setHotel_bookings(null);
        hotels.save(hotel);
        return new StatusCodeDTO("Hotel modificado correctamente");
    }
/**
     * Obtiene una lista de todos los hoteles disponibles.
     */

    public List<HotelDTO> obtenerHoteles(){
        List<Hotel> hotelList = hotels.findAll();
        if (hotelList.isEmpty())
            throw new NoContentException("No se encontraron hoteles.");
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            HotelDTO hotelDTO = transformarHotelAHotelDTO(hotel);
            hotelDTOList.add(hotelDTO);
        }
        return hotelDTOList;
    }

    /**
     * Obtiene una lista de los hoteles disponibles filtrando en base a los parametros.
     * @param dateFrom La fecha inicial.
     * @param dateTo La fecha final.
     * @param destination El lugar destino.
     */
    public List<HotelDTO> obtenerHotelesDisponibles(LocalDate dateFrom, LocalDate dateTo, String destination){
        if (dateFrom.compareTo(dateTo) >= 0)
            throw new ConflictException("La fecha de salida debe ser mayor a la de entrada.");
        List<Hotel> hotelList = hotels.findAll();
        List<String> destino = hotelList.stream()
                .map(Hotel::getPlace)
                .distinct()
                .collect(Collectors.toList());
        if (!destino.contains(destination))
            throw new NoContentException("El destino elegido no existe.");
        Date disponibilityDateFrom= java.util.Date.from(dateFrom.atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant());
        Date disponibilityDateTo  = java.util.Date.from(dateTo.atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant());
        List<Hotel> hotelListFiltered = hotelList.stream()
                .filter(x -> (x.getDisponibilityDateFrom().compareTo(disponibilityDateFrom) <= 0)
                        && (x.getDisponibilityDateTo().compareTo(disponibilityDateTo) >= 0)
                        && x.getPlace().equals(destination))
                .collect(Collectors.toList());
        if (hotelListFiltered.isEmpty())
            throw new NoContentException("No se encontraron hoteles disponibles para esta busqueda.");
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for (Hotel hotel : hotelListFiltered) {
            HotelDTO hotelDTO = transformarHotelAHotelDTO(hotel);
            hotelDTOList.add(hotelDTO);
        }
        return hotelDTOList;
    }

    /**
     * Realiza la reserva de un hotel en base a el objeto payloadHotelDTO.
     * @param payloadHotelDTO Objeto con los datos para realizar una reserva de hotel.
     */
    public StatusCodeDTO reservar(PayloadHotelDTO payloadHotelDTO){
        if (payloadHotelDTO.getBooking().getDateFrom().compareTo(payloadHotelDTO.getBooking().getDateTo()) >= 0)
            throw new ConflictException("La fecha de salida debe ser mayor a la de entrada.");
        List<Hotel> hotelList = hotels.findAll();
        List<String> destinos = hotelList.stream()
                .map(Hotel::getPlace)
                .distinct()
                .collect(Collectors.toList());
        if (!destinos.contains(payloadHotelDTO.getBooking().getDestination()))
            throw new NoContentException("El destino elegido no existe.");
        validarTipoHabitacionCantidadPersonas(payloadHotelDTO.getBooking());
        Hotel hotel = hotels.getById(payloadHotelDTO.getBooking().getHotelCode());
        if (hotel.getIsBooking())
            throw new ConflictException("Este hotel ya se encuentra reservado.");

        Period period = Period.between(payloadHotelDTO.getBooking().getDateFrom(), payloadHotelDTO.getBooking().getDateTo());

        Double interests = calculaIntereses(payloadHotelDTO.getBooking().getPaymentMethod());
        double amount = hotel.getRoomPrice() * (period.getDays()-1);
        Double total = amount * (1 + interests);

        PaymentMethod paymentMethodSaved = savePaymentMethod(payloadHotelDTO.getBooking().getPaymentMethod());

        Hotel_booking hotel_bookingSaved = saveHotel_booking(payloadHotelDTO, paymentMethodSaved.getPaymentMethod_id(), total);

        List<PersonDTO> personDTOList = payloadHotelDTO.getBooking().getPeople();
        List<Person> personList = new ArrayList<>();
        for (PersonDTO personDTO : personDTOList) {
            personList.add(transformarPersonDTOAPerson(personDTO));
        }
        savePeople(personList, hotel_bookingSaved);

        hotel.setIsBooking(true);
        hotels.save(hotel);

        return new StatusCodeDTO("El proceso termino satisfactoriamente.");
    }

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

    /**
     * Valida que el tipo de habitación seleccionada coincida con la cantidad de personas que se alojarán en ella.
     * @param bookingPayloadDTO Objeto con los datos del tipo de habitación, y cantidad de personas.
     */
    private void validarTipoHabitacionCantidadPersonas(BookingPayloadDTO bookingPayloadDTO){
        String message = "El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella.";
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("SINGLE") && bookingPayloadDTO.getPeopleAmount() != 1)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("DOUBLE") && bookingPayloadDTO.getPeopleAmount() != 2)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("TRIPLE") && bookingPayloadDTO.getPeopleAmount() != 3)
            throw new ConflictException(message);
        if (bookingPayloadDTO.getRoomType().equalsIgnoreCase("MULTIPLE") && ((bookingPayloadDTO.getPeopleAmount() < 4) || (bookingPayloadDTO.getPeopleAmount() > 6)))
            throw new ConflictException(message);
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
     * Graba la reserva del hotel.
     * @param payloadHotelDTO Objeto con los datos necesarios guardar la reserva del vuelo.
     * @param paymentMethod_id Id del método de pago.
     * @param total Monto total de la reservación.
     */
    private Hotel_booking saveHotel_booking(PayloadHotelDTO payloadHotelDTO, Integer paymentMethod_id, Double total){
        Hotel_booking hotel_booking = new Hotel_booking();
        hotel_booking.setUserName(payloadHotelDTO.getUserName());
        hotel_booking.setDateFrom(java.util.Date.from(
                payloadHotelDTO.getBooking().getDateFrom()
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        hotel_booking.setDateTo(java.util.Date.from(
                payloadHotelDTO.getBooking().getDateTo()
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        hotel_booking.setDestination(payloadHotelDTO.getBooking().getDestination());
        hotel_booking.setHotelCode(payloadHotelDTO.getBooking().getHotelCode());
        hotel_booking.setPeopleAmount(payloadHotelDTO.getBooking().getPeopleAmount());
        hotel_booking.setRoomType(payloadHotelDTO.getBooking().getRoomType());
        hotel_booking.setPaymentMethod_id(paymentMethod_id);
        hotel_booking.setTotal(total);
        return hotel_booking;
    }

    /**
     * Graba a la lista de personas y sus reservas.
     * @param personList Objeto con los datos necesarios guardar el metodo de pago.
     * @param hotel_booking Id de la reservación.
     */
    private void savePeople(List<Person> personList, Hotel_booking hotel_booking){
        for (Person person : personList) {
            if (!people.existsById(person.getDni()))
                people.save(person);
            Booking_person booking_person = new Booking_person();
            booking_person.setHotel_booking(hotel_booking);
            booking_person.setPerson(person);
            booking_people.save(booking_person);
        }
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
     * Transforma de un objeto BookingPayloadDTO a BookingResponseDTO.
     * @param bookingPayloadDTO Objeto con los datos necesarios para hacer la transformación.
     *//*

    private BookingResponseDTO transformarBookingPayloadABookingResponse(BookingPayloadDTO bookingPayloadDTO){
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setDateFrom(bookingPayloadDTO.getDateFrom());
        bookingResponseDTO.setDateTo(bookingPayloadDTO.getDateTo());
        bookingResponseDTO.setDestination(bookingPayloadDTO.getDestination());
        bookingResponseDTO.setHotelCode(bookingPayloadDTO.getHotelCode());
        bookingResponseDTO.setPeopleAmount(bookingPayloadDTO.getPeopleAmount());
        bookingResponseDTO.setRoomType(bookingPayloadDTO.getRoomType());
        bookingResponseDTO.setPeople(bookingPayloadDTO.getPeople());
        return bookingResponseDTO;
    }

    */
    /**
     * Transforma de un objeto Hotel a HotelDTO.
     * @param hotel Objeto con los datos necesarios para hacer la transformación.
     */
    private HotelDTO transformarHotelAHotelDTO(Hotel hotel){
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelCode(hotel.getHotelCode());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setPlace(hotel.getPlace());
        hotelDTO.setRoomType(hotel.getRoomType());
        hotelDTO.setRoomPrice(hotel.getRoomPrice());
        hotelDTO.setDisponibilityDateFrom(hotel.getDisponibilityDateFrom().toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate());
        hotelDTO.setDisponibilityDateTo(hotel.getDisponibilityDateTo().toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDate());
        hotelDTO.setBooking(hotel.getIsBooking());

        return hotelDTO;
    }

    /**
     * Transforma de un objeto HotelDTO a Hotel.
     * @param hotelDTO Objeto con los datos necesarios para hacer la transformación.
     */
    private Hotel transformarHotelDTOAHotel(HotelDTO hotelDTO){
        Hotel hotel = new Hotel();
        hotel.setHotelCode(hotelDTO.getHotelCode());
        hotel.setName(hotelDTO.getName());
        hotel.setPlace(hotelDTO.getPlace());
        hotel.setRoomType(hotelDTO.getRoomType());
        hotel.setRoomPrice(hotelDTO.getRoomPrice());
        hotel.setDisponibilityDateFrom(java.util.Date.from(hotelDTO.getDisponibilityDateFrom().atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant()));
        hotel.setDisponibilityDateTo(java.util.Date.from(hotelDTO.getDisponibilityDateTo().atStartOfDay().
                atZone(ZoneId.systemDefault()).
                toInstant()));
        hotel.setIsBooking(hotelDTO.isBooking());

        return hotel;
    }

}