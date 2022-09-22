package ru.practicum.diplom.priv.event;

import lombok.Data;
import ru.practicum.diplom.admin.category.Category;
import ru.practicum.diplom.admin.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "title", length = 120, nullable = false)
    private String title;
    @Column(name = "annotation", length = 2000)
    private String annotation;
    @NotNull
    @Column(name = "description", length = 7000)
    private String description;
    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;
    private boolean paid;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    private Double longitude;
    private Double latitude;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 100, nullable = false)
    private EventState state;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator")
    private User initiator;

}
