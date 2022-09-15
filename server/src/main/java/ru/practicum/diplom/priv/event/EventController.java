package ru.practicum.diplom.priv.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.service.EventService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public EventFullDto saveEvent(
            @Valid @RequestBody NewEventDto eventDto,
            @PathVariable Long userId) {
        return eventService.saveEvent(eventDto, userId);
    }
}
