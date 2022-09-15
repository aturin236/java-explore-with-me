package ru.practicum.diplom.priv.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.category.repository.CategoryRepository;
import ru.practicum.diplom.admin.user.repository.UserRepository;
import ru.practicum.diplom.exceptions.EventBadRequestException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.EventMapper;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public EventFullDto saveEvent(NewEventDto newEventDto, Long userId) {
        log.debug("Запрос saveEvent с title - {}", newEventDto.getTitle());

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            String eventDate = newEventDto.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            throw new EventBadRequestException(
                    String.format("Событие начинается %s. Это очень рано. Никто не придет", eventDate)
            );
        }

        Event event = EventMapper.newEventDtoToEvent(newEventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryRepository.checkAndReturnCategoryIfExist(newEventDto.getCategory()));
        event.setInitiator(userRepository.checkAndReturnUserIfExist(userId));
        event.setState(EventState.PENDING);

        return EventMapper.eventToEventFullDto(
                eventRepository.save(event)
        );
    }
}
