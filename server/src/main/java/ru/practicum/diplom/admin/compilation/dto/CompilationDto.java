package ru.practicum.diplom.admin.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    @NotNull
    private String title;
    private boolean pinned;
    private List<Long> events;
}
