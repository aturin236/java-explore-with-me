package ru.practicum.diplom.admin.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.admin.compilation.dto.CompilationDto;
import ru.practicum.diplom.admin.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto saveCompilation(@Valid @RequestBody CompilationDto compilationDto) {
        return compilationService.saveCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventFromCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompilationFromDesk(@PathVariable Long compId) {
        compilationService.compilationPinnedOff(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompilationToDesk(@PathVariable Long compId) {
        compilationService.compilationPinnedOn(compId);
    }
}
