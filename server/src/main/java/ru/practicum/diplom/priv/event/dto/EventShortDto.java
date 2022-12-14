package ru.practicum.diplom.priv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private String annotation;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private int confirmedRequests;
    private Long id;
    private CategoryDto category;
    private boolean paid;
    private UserShortDto initiator;
    private long views;
    private long eventRating;
    private long eventInitiatorRating;
}
