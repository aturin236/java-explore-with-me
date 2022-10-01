package ru.practicum.diplom.publics.event.service;

import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.publics.event.EventSortKey;

import javax.servlet.http.HttpServletRequest;
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
            EventSortKey sort,
            int from,
            int size);

    EventShortDto getEvent(Long id);

    EventShortDto saveStat(EventShortDto eventShortDto, HttpServletRequest request);

    List<EventShortDto> saveStat(List<EventShortDto> dtoCollection, HttpServletRequest request);
}
