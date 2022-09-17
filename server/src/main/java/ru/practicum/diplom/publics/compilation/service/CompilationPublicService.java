package ru.practicum.diplom.publics.compilation.service;

import ru.practicum.diplom.admin.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(
            Boolean pinned,
            int from,
            int size);

    CompilationDto getCompilationById(Long id);
}
