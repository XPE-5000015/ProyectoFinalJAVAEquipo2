package com.example.demo.repository;

import com.example.demo.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Flights extends JpaRepository<Flight, String> {
}