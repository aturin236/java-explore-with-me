package ru.practicum.diplom.publics.event.service;

import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.publics.event.EventKindSort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventKindSort sort,
            int from,
            int size);

    EventShortDto getEvent(Long id);
}
