package ru.practicum.diplom.priv.event.dto;

import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.user.dto.UserMapper;
import ru.practicum.diplom.priv.event.Event;

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
                .build();
    }
}
