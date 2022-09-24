package ru.practicum.diplom.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.practicum.diplom.json.CustomDateDeserializer;

import java.time.LocalDateTime;

@Data
public class EventStatDto {
    private String app;
    private String uri;
    private String ip;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime dateHit;
}
