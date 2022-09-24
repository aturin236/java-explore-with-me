package ru.practicum.diplom.priv.ratings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.ratings.model.EventRating;

import java.util.List;
import java.util.Optional;

public interface EventRatingRepository extends JpaRepository<EventRating, Long> {
    Optional<EventRating> findEventRatingByEvent(Event event);

    @Query(value = "select * from event_ratings er where er.event in (?1)", nativeQuery = true)
    List<EventRating> findEventRatingsByEvents(List<Long> events);
}
