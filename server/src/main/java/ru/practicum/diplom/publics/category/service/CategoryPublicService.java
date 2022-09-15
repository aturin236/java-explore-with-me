package ru.practicum.diplom.publics.category.service;

import ru.practicum.diplom.admin.category.dto.CategoryDto;

public interface CategoryPublicService {
    Iterable<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long id);
}
