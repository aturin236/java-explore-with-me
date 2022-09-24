package ru.practicum.diplom.admin.compilation.dto;

import lombok.Data;
import ru.practicum.diplom.priv.event.dto.EventFullDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    @NotNull
    private String title;
    private boolean pinned;
    private List<EventFullDto> events;
}
