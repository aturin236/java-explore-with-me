package ru.practicum.diplom.admin.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.category.Category;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.category.repository.CategoryRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        log.debug("Запрос saveCategory по name - {}", categoryDto.getName());
        return CategoryMapper.toCategoryDto(
                categoryRepository.save(
                        CategoryMapper.toCategory(categoryDto)
                )
        );
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.debug("Запрос updateCategory по id - {} и name - {}", categoryDto.getId(), categoryDto.getName());
        categoryRepository.checkAndReturnCategoryIfExist(categoryDto.getId());
        return CategoryMapper.toCategoryDto(
                categoryRepository.save(
                        CategoryMapper.toCategory(categoryDto)
                )
        );
    }

    @Override
    public void deleteCategory(Long id) {
        log.debug("Запрос deleteCategory по id - {}", id);
        Category category = categoryRepository.checkAndReturnCategoryIfExist(id);
        categoryRepository.delete(category);
    }
}
