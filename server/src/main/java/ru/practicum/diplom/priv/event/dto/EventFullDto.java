package ru.practicum.diplom.priv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.user.dto.UserShortDto;
import ru.practicum.diplom.priv.event.EventState;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {
    private String annotation;
    private String description;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private int confirmedRequests;
    private Long id;
    private CategoryDto category;
    private boolean paid;
    private boolean requestModeration;
    private Integer participantLimit;
    private UserShortDto initiator;
    private Location location;
    private EventState state;
    private int views;
}
