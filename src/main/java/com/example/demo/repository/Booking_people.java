package com.example.demo.repository;

import com.example.demo.entity.Booking_person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Booking_people extends JpaRepository<Booking_person, Booking_person> {
}
