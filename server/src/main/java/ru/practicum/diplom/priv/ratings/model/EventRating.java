package ru.practicum.diplom.priv.ratings.model;

import lombok.Data;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;

@Entity
@IdClass(value = EventRatingId.class)
@Table(name = "event_ratings")
@Data
public class EventRating {
    @Id
    @Column(name = "event")
    private Long eventId;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event", insertable = false, updatable = false)
    private Event event;

    @Column(name = "likes_count")
    private long likesCount;
    @Column(name = "dislikes_count")
    private long dislikesCount;
}
