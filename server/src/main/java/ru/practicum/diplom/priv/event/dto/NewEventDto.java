package ru.practicum.diplom.priv.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.diplom.priv.event.Event;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewEventDto extends UpdateEventDto {
    private Boolean requestModeration;
    private Location location;

    @Override
    public void setFieldsToEvent(Event event) {
        super.setFieldsToEvent(event);
        if (requestModeration != null) event.setRequestModeration(requestModeration);
        if (location != null) {
            event.setLatitude(location.getLat());
            event.setLongitude(location.getLon());
        }
    }
}
