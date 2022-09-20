package ru.practicum.diplom.admin.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.compilation.Compilation;
import ru.practicum.diplom.admin.compilation.dto.CompilationDto;
import ru.practicum.diplom.admin.compilation.dto.CompilationMapper;
import ru.practicum.diplom.admin.compilation.dto.NewCompilationDto;
import ru.practicum.diplom.admin.compilation.repository.CompilationRepository;
import ru.practicum.diplom.exceptions.CompilationBadRequestException;
import ru.practicum.diplom.exceptions.EventNotFoundException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.repository.EventRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        log.debug("Добавление новой подборки с title {}", compilationDto.getTitle());

        Compilation compilation = CompilationMapper.compilationFromDto(compilationDto);
        compilation.setEvents(eventRepository.findEventsByIdIn(compilationDto.getEvents()));

        return CompilationMapper.compilationToDto(
                compilationRepository.save(compilation)
        );
    }

    @Override
    public void deleteCompilation(Long id) {
        log.debug("Удаление новой подборки с id {}", id);
        compilationRepository.delete(
                compilationRepository.checkAndReturnCompilationIfExist(id)
        );
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.debug("Удаление события {} из подборки {}", eventId, compId);

        Event event = eventRepository.checkAndReturnEventIfExist(eventId);
        Compilation compilation = compilationRepository.checkAndReturnCompilationIfExist(compId);

        if (!compilation.getEvents().contains(event)) {
            throw new EventNotFoundException(
                    String.format("Событие %s отсутствует в подборке %s", eventId, compId)
            );
        }
        List<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);

        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        log.debug("Добавление события {} из подборки {}", eventId, compId);

        Event event = eventRepository.checkAndReturnEventIfExist(eventId);
        Compilation compilation = compilationRepository.checkAndReturnCompilationIfExist(compId);

        if (compilation.getEvents().contains(event)) {
            throw new CompilationBadRequestException(
                    String.format("Событие %s уже есть в подборке %s", eventId, compId)
            );
        }
        List<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);

        compilationRepository.save(compilation);
    }

    @Override
    public void compilationPinnedOff(Long id) {
        log.debug("Открепить подборку {}", id);

        Compilation compilation = compilationRepository.checkAndReturnCompilationIfExist(id);
        if (compilation.isPinned()) {
            compilation.setPinned(false);
            compilationRepository.save(compilation);
        }
    }

    @Override
    public void compilationPinnedOn(Long id) {
        log.debug("Закрепить подборку {}", id);

        Compilation compilation = compilationRepository.checkAndReturnCompilationIfExist(id);
        if (!compilation.isPinned()) {
            compilation.setPinned(true);
            compilationRepository.save(compilation);
        }
    }
}
