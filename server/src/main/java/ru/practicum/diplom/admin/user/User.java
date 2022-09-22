package ru.practicum.diplom.admin.user;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 250, nullable = false)
    //@Pattern(regexp = "^[a-zA-Z\\d-._]+$")
    private String name;
    @Column(name = "email", length = 150, nullable = false)
    private String email;
}