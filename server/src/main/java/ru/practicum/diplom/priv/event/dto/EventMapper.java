package ru.practicum.diplom.priv.event.dto;

import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.user.dto.UserMapper;
import ru.practicum.diplom.priv.event.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static Event newEventDtoToEvent(NewEventDto eventDto) {
        Event event = updateEventDtoToEvent(eventDto);
        event.setId(null);
        event.setRequestModeration(eventDto.getRequestModeration());
        Location location = eventDto.getLocation();
        if (location != null) {
            event.setLatitude(location.getLat());
            event.setLongitude(location.getLon());
        }

        return event;
    }

    public static Event updateEventDtoToEvent(UpdateEventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getEventId());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setTitle(eventDto.getTitle());
        event.setEventDate(eventDto.getEventDate());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());

        return event;
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setState(event.getState());
        eventFullDto.setLocation(new Location(event.getLongitude(), event.getLatitude()));

        return eventFullDto;
    }

    public static List<EventFullDto> eventToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        events.forEach(x -> dtos.add(eventToEventFullDto(x)));

        return dtos;
    }

    public static EventShortDto eventToEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setDescription(event.getDescription());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));

        return eventShortDto;
    }

    public static List<EventShortDto> eventToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        events.forEach(x -> dtos.add(eventToEventShortDto(x)));

        return dtos;
    }
}
