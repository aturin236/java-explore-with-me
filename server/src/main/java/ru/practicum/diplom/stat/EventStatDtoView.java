package ru.practicum.diplom.stat;

import lombok.Data;

@Data
public class EventStatDtoView {
    private String app;
    private String uri;
    private Long hits;
}
