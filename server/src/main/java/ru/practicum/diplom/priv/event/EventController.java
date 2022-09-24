package ru.practicum.diplom.priv.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.dto.UpdateEventDto;
import ru.practicum.diplom.priv.event.service.EventService;
import ru.practicum.diplom.priv.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

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

    @PatchMapping
    public EventFullDto updateEvent(
            @Valid @RequestBody UpdateEventDto eventDto,
            @PathVariable Long userId) {
        return eventService.updateEvent(eventDto, userId);
    }

    @GetMapping
    public List<EventFullDto> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size) {
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getEventRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmEventRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) {
        return eventService.confirmEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectEventRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) {
        return eventService.rejectEventRequest(userId, eventId, reqId);
    }
}
