package com.example.demo.repository;

import com.example.demo.entity.Flight_reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Flight_reservations extends JpaRepository<Flight_reservation, Integer> {
}
