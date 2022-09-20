package ru.practicum.diplom.dto;

import ru.practicum.diplom.EventStat;

public class EventStatMapper {
    public static EventStat eventStatFromDto(EventStatDto eventStatDto) {
        EventStat eventStat = new EventStat();
        eventStat.setApp(eventStatDto.getApp());
        eventStat.setUri(eventStatDto.getUri());
        eventStat.setIp(eventStatDto.getIp());
        eventStat.setDate_hit(eventStatDto.getDate_hit());

        return eventStat;
    }

    public static EventStatDtoView eventStatToDtoView(EventStat eventStat) {
        EventStatDtoView dtoView = new EventStatDtoView();
        dtoView.setApp(eventStat.getApp());
        dtoView.setUri(eventStat.getUri());

        return dtoView;
    }
}
