package ru.practicum.diplom.priv.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.exceptions.EventNotFoundException;
import ru.practicum.diplom.priv.event.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    default Event checkAndReturnEventIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Событие с id=%s не найден", id)));
    }

    List<Event> findEventsByIdIn(List<Long> ids);

    List<Event> findEventsByInitiator(User user, Pageable pageable);
}
