package com.example.demo.repository;

import com.example.demo.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Flights extends JpaRepository<Flight, String> {

/*
    @Query("SELECT DISTINCT f.origin FROM Flights f")
    List<String> findAllOrigin();

    @Query("SELECT DISTINCT f.destination FROM Flights f")
    List<String> findAllDestination();
*/
}