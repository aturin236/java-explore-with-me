package ru.practicum.diplom.admin.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.diplom.admin.compilation.Compilation;
import ru.practicum.diplom.exceptions.CompilationNotFoundException;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {
    default Compilation checkAndReturnCompilationIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new CompilationNotFoundException(String.format("Компиляция с id=%s не найдена", id)));
    }

    Compilation findCompilationById(Long id);
}
