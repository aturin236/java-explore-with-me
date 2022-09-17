package ru.practicum.diplom.publics.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.exceptions.EventForbiddenException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.dto.EventMapper;
import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.priv.event.repository.EventRepository;
import ru.practicum.diplom.publics.event.EventKindSort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;

    @Override
    public List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventKindSort sort,
            int from,
            int size) {
        List<Event> events = eventRepository.findEventsByParam(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                sort,
                from,
                size
        );
        return EventMapper.eventToEventShortDto(events);//TODO добавить сортировку и поля количества реквестов и view
    }

    @Override
    public EventShortDto getEvent(Long id) {
        log.debug("Запрос getEvent по id - {}", id);
        Event event = eventRepository.checkAndReturnEventIfExist(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventForbiddenException(
                    String.format("Попытка получения не опубликованного события по id %s", id)
            );
        }
        return EventMapper.eventToEventShortDto(event);
    }
}
