package ru.practicum.diplom.priv.ratings.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;

@Entity
@IdClass(value = EventLikeId.class)
@Table(name = "event_likes")
@Data
@NoArgsConstructor
public class EventLike {
    @Id
    @Column(name = "event")
    private Long eventId;

    @Id
    @Column(name = "requester")
    private Long requesterId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event", insertable = false, updatable = false)
    private Event event;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester", insertable = false, updatable = false)
    private User requester;

    @Column(name = "is_like")
    private boolean isLike;

    public EventLike(Event event, User requester) {
        this.event = event;
        this.requester = requester;
        this.eventId = event.getId();
        this.requesterId = requester.getId();
    }
}
