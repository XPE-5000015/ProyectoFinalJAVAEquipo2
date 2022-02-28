package com.example.demo.repository;

import com.example.demo.entity.Reservation_person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Reservation_people extends JpaRepository<Reservation_person, Reservation_person> {
}
