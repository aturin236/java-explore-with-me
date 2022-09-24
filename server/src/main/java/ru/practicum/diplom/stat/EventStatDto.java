package ru.practicum.diplom.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.practicum.diplom.json.CustomDateDeserializer;

import java.time.LocalDateTime;

@Data
public class EventStatDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime dateHit;
}
