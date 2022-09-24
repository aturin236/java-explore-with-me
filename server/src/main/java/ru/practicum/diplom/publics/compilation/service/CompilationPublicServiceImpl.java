package ru.practicum.diplom.publics.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.compilation.dto.CompilationDto;
import ru.practicum.diplom.admin.compilation.dto.CompilationMapper;
import ru.practicum.diplom.admin.compilation.repository.CompilationRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        log.debug("Запрос getCompilations pinned {}, from {}, size {}", pinned, from, size);
        return CompilationMapper.compilationToDto(
                compilationRepository.findCompilationsByParam(pinned, from, size)
        );
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        log.debug("Запрос getCompilationById id {}", id);
        return CompilationMapper.compilationToDto(
                compilationRepository.findCompilationById(id)
        );
    }
}
