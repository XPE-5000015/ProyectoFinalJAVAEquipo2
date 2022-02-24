# Agencia de viajes


Este proyecto contiene API's para gestionar una agencia de viajes, mediante EndPoints

-localhost:8080/api/v1/hotels
Otorga la lista de todos los hoteles disponibles.

-localhost:8080/api/v1/hotels?dateFrom=12/02/2022&dateTo=19/03/2022&destination=Buenos Aires
Otorga una lista de los hoteles disponibles filtrando en base a los parametros.

-localhost:8080/api/v1/booking
Realiza la reserva de un hotel en base a el objeto payloadHotelDTO.

-localhost:8080/api/v1/flights
Otorga la lista de todos los vuelos disponibles.

-localhost:8080/api/v1/flights?dateFrom=11/02/2022&dateTo=18/02/2022&origin=Puerto Iguazú&destination=Bogotá
Otorga una lista de los vuelos disponibles filtrando en base a los parametros.

-localhost:8080/api/v1/flight-reservation
Realiza la reserva de un vuelo en base a el objeto payloadHotelDTO.