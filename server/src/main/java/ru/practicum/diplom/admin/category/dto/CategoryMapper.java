package ru.practicum.diplom.admin.category.dto;

import ru.practicum.diplom.admin.category.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> dtos = new ArrayList<>();
        categories.forEach(x -> dtos.add(toCategoryDto(x)));

        return dtos;
    }

    public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }
}
