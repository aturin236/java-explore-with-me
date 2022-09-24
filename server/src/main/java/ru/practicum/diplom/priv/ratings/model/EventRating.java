package ru.practicum.diplom.priv.ratings.model;

import lombok.Data;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;

@Entity
@Table(name = "event_ratings")
@Data
public class EventRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event")
    private Event event;
    @Column(name = "count_like")
    private long countLike;
    @Column(name = "count_dislike")
    private long countDislike;
}
