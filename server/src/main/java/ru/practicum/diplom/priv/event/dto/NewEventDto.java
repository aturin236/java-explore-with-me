package ru.practicum.diplom.priv.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewEventDto extends UpdateEventDto {
    private boolean requestModeration;
    private Location location;
}
