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
    private String title;
    private String annotation;
    @NotNull
    private String description;
    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NotNull
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    private boolean paid;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    private Double longitude;
    private Double latitude;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator")
    private User initiator;

}
