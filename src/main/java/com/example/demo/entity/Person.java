package com.example.demo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "people")
public class Person
{
    @Id
    private String dni;
    private String name;
    private String lastname;
    private Date birthDate;
    private String mail;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Reservation_person> reservation_people;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Booking_person> booking_people;
}
