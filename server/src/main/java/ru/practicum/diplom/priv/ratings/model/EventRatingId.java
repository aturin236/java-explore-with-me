package ru.practicum.diplom.priv.ratings.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class EventRatingId implements Serializable {
    private Long eventId;
}
