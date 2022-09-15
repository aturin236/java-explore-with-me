package ru.practicum.diplom.priv.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import ru.practicum.diplom.json.CustomDateDeserializer;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "annotation must be between 20 and 2000 characters")
    private String annotation;
    @Size(min = 20, max = 7000, message = "annotation must be between 20 and 7000 characters")
    private String description;
    @Size(min = 3, max = 120, message = "annotation must be between 3 and 120 characters")
    private String title;
    @NotNull
    @Future
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime eventDate;
    private Long category;
    private boolean paid;
    private boolean requestModeration;
    private Integer participantLimit;
    private Location location;
}
