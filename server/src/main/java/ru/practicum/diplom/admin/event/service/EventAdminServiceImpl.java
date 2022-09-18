package ru.practicum.diplom.admin.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.category.repository.CategoryRepository;
import ru.practicum.diplom.exceptions.EventForbiddenException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.EventMapper;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.dto.service.EventDtoService;
import ru.practicum.diplom.priv.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventDtoService eventDtoService;

    @Override
    public List<EventFullDto> getEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size) {
        return eventDtoService.addConfirmedRequests(
                EventMapper.eventToEventFullDto(
                        eventRepository.findEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size)
                )
        );
    }

    @Override
    public EventFullDto updateEvent(Long id, NewEventDto newEventDto) {
        log.debug("Запрос updateEvent по id - {}", id);

        Event oldEvent = eventRepository.checkAndReturnEventIfExist(id);

        Event event = EventMapper.newEventDtoToEvent(newEventDto);
        event.setCategory(categoryRepository.checkAndReturnCategoryIfExist(newEventDto.getCategory()));
        event.setId(oldEvent.getId());
        event.setCreatedOn(oldEvent.getCreatedOn());
        event.setPublishedOn(oldEvent.getPublishedOn());
        event.setInitiator(oldEvent.getInitiator());
        event.setState(oldEvent.getState());

        return eventDtoService.addConfirmedRequests(
                EventMapper.eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public EventFullDto publishEvent(Long id) {
        log.debug("Запрос publishEvent по id - {}", id);

        Event event = eventRepository.checkAndReturnEventIfExist(id);

        LocalDateTime datePublish = LocalDateTime.now();

        if (event.getEventDate().isBefore(datePublish.plusHours(1))) {
            throw new EventForbiddenException("Время публикации события вышло");
        }

        if (event.getState() != EventState.PENDING) {
            throw new EventForbiddenException(
                    String.format("Событие должно быть в статусе PENDING, но имеет статус %s", event.getState())
            );
        }

        event.setPublishedOn(datePublish);
        event.setState(EventState.PUBLISHED);

        return eventDtoService.addConfirmedRequests(
                EventMapper.eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public EventFullDto rejectEvent(Long id) {
        log.debug("Запрос rejectEvent по id - {}", id);

        Event event = eventRepository.checkAndReturnEventIfExist(id);

        if (event.getState() == EventState.PUBLISHED) {
            throw new EventForbiddenException("Событие уже опубликовано");
        }

        event.setState(EventState.CANCELED);

        return eventDtoService.addConfirmedRequests(
                EventMapper.eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }
}
