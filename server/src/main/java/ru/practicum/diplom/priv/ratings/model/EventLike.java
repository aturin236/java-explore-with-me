package ru.practicum.diplom.priv.ratings.model;

import lombok.Data;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;

@Entity
@Table(name = "event_likes")
@Data
public class EventLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester")
    private User requester;
    @Column(name = "is_like")
    private boolean isLike;
}
