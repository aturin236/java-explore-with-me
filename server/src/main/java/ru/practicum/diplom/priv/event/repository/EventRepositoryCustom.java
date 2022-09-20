package ru.practicum.diplom.priv.event.repository;

import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findEventsByParam(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size);

    List<Event> findEventsByParam(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size);
}
