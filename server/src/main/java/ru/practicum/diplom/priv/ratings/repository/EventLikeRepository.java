package ru.practicum.diplom.priv.ratings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.ratings.model.EventLike;
import ru.practicum.diplom.priv.ratings.model.EventLikeId;

import java.util.Optional;

public interface EventLikeRepository extends JpaRepository<EventLike, EventLikeId> {
    Optional<EventLike> findByEventAndRequester(Event event, User user);
}
