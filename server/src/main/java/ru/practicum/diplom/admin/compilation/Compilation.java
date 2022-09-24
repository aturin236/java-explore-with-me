package ru.practicum.diplom.admin.compilation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.diplom.priv.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "compilation")
@Data
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "title", length = 250, nullable = false)
    private String title;
    private boolean pinned;
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;
}
