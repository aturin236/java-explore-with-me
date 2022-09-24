package ru.practicum.diplom.admin.category.service;

import ru.practicum.diplom.admin.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);
}
