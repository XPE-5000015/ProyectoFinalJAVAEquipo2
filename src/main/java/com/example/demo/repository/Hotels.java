package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Hotels extends JpaRepository<Hotel, String> {
}