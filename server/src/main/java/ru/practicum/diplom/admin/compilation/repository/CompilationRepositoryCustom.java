package ru.practicum.diplom.admin.compilation.repository;

import ru.practicum.diplom.admin.compilation.Compilation;

import java.util.List;

public interface CompilationRepositoryCustom {
    List<Compilation> findCompilationsByParam(Boolean pinned, int from, int size);
}
