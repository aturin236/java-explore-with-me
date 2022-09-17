package ru.practicum.diplom.admin.compilation.service;

import ru.practicum.diplom.admin.compilation.dto.CompilationDto;

public interface CompilationService {
    CompilationDto saveCompilation(CompilationDto compilationDto);

    void deleteCompilation(Long id);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void compilationPinnedOff(Long id);

    void compilationPinnedOn(Long id);
}
