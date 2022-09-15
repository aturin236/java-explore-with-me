package ru.practicum.diplom.admin.category;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
