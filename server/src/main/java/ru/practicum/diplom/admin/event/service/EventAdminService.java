package ru.practicum.diplom.admin.event.service;

import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getEvents(List<Long> users,
                                 List<EventState> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 int from,
                                 int size);

    EventFullDto updateEvent(Long id, NewEventDto newEventDto);

    EventFullDto publishEvent(Long id);

    EventFullDto rejectEvent(Long id);
}
