package ru.practicum.diplom.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hit")
@Data
public class EventStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app", length = 250)
    private String app;
    @Column(name = "uri", length = 250)
    private String uri;
    @Column(name = "ip", length = 50)
    private String ip;
    @Column(name = "date_hit")
    private LocalDateTime dateHit;

}
