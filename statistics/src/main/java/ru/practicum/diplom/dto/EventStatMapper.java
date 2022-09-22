package ru.practicum.diplom.dto;

import ru.practicum.diplom.EventStat;

public class EventStatMapper {
    public static EventStat eventStatFromDto(EventStatDto eventStatDto) {
        EventStat eventStat = new EventStat();
        eventStat.setApp(eventStatDto.getApp());
        eventStat.setUri(eventStatDto.getUri());
        eventStat.setIp(eventStatDto.getIp());
        eventStat.setDateHit(eventStatDto.getDateHit());

        return eventStat;
    }

}
