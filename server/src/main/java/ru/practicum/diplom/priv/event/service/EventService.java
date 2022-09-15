package ru.practicum.diplom.priv.event.service;

import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;

public interface EventService {
    EventFullDto saveEvent(NewEventDto newEventDto, Long userId);
}
