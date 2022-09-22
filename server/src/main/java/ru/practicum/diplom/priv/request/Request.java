package ru.practicum.diplom.priv.request;

import lombok.Data;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_request")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester")
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100, nullable = false)
    private RequestStatus status;
}
