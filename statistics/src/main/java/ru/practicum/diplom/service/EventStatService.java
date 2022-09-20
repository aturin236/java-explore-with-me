package ru.practicum.diplom.service;

import ru.practicum.diplom.dto.EventStatDto;
import ru.practicum.diplom.dto.EventStatDtoView;

import java.time.LocalDateTime;
import java.util.List;

public interface EventStatService {
    void save(EventStatDto eventStatDto);

    List<EventStatDtoView> getEventStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean unique
    );
}
