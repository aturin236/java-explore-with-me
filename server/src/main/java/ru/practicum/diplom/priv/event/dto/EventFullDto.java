package ru.practicum.diplom.priv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.diplom.priv.event.EventState;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private Integer participantLimit;
    private Location location;
    private EventState state;
}
