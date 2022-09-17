package ru.practicum.diplom.publics.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.exceptions.EventBadRequestException;
import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.publics.event.service.EventPublicService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventPublicService eventPublicService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(defaultValue = "EVENT_DATE", required = false) EventKindSort sort,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size) {
        LocalDateTime rangeStartDate = null;
        LocalDateTime rangeEndDate = null;

        if (rangeStart != null) {
            try {
                rangeStartDate = LocalDateTime.parse(rangeStart);
            } catch (DateTimeParseException e) {
                throw new EventBadRequestException(
                        String.format("Неверный формат даты rangeStart %s", rangeStart)
                );
            }
        }

        if (rangeEnd != null) {
            try {
                rangeEndDate = LocalDateTime.parse(rangeEnd);
            } catch (DateTimeParseException e) {
                throw new EventBadRequestException(
                        String.format("Неверный формат даты rangeEnd %s", rangeEnd)
                );
            }
        }

        return eventPublicService.getEvents(
                text,
                categories,
                paid,
                rangeStartDate,
                rangeEndDate,
                onlyAvailable,
                sort,
                from,
                size);
    }

    @GetMapping("/{id}")
    public EventShortDto getEvent(@PathVariable Long id) {
        return eventPublicService.getEvent(id);
    }
}
