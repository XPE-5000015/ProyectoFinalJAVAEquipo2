package com.example.demo.repository;

import com.example.demo.entity.Hotel_booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Hotel_bookings extends JpaRepository<Hotel_booking, Integer> {
}
