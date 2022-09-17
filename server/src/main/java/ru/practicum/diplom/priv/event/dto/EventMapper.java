package ru.practicum.diplom.priv.event.dto;

import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.user.dto.UserMapper;
import ru.practicum.diplom.priv.event.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static Event newEventDtoToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setTitle(newEventDto.getTitle());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.isPaid());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        Location location = newEventDto.getLocation();
        if (location != null) {
            event.setLatitude(location.getLat());
            event.setLongitude(location.getLon());
        }

        return event;
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.isPaid())
                .requestModeration(event.isRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .state(event.getState())
                .location(new Location(event.getLongitude(), event.getLatitude()))
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    public static List<EventFullDto> eventToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(eventToEventFullDto(event));
        }
        return dtos;
    }

    public static EventShortDto eventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.isPaid())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    public static List<EventShortDto> eventToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(eventToEventShortDto(event));
        }
        return dtos;
    }
}
