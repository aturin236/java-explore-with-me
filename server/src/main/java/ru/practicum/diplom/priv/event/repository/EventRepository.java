package ru.practicum.diplom.priv.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.diplom.exceptions.EventNotFoundException;
import ru.practicum.diplom.priv.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    default Event checkAndReturnEventIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Событие с id=%s не найден", id)));
    }
}
