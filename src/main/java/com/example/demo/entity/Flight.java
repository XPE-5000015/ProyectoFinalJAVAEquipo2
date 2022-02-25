package com.example.demo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    private String flightNumber;
    private String name;
    private String origin;
    private String destination;
    private String seatType;
    private Double flightPrice;
    private Date goingDate;
    private Date returnDate;
}