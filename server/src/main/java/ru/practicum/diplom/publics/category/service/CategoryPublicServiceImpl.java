package ru.practicum.diplom.publics.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.category.repository.CategoryRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryRepository categoryRepository;

    @Override
    public Iterable<CategoryDto> getCategories(int from, int size) {
        log.debug("Запрос getCategories from - {}, page - {}", from, size);

        Pageable pageRequest = PageRequest.of(from, size);

        return CategoryMapper.toCategoryDto(
                categoryRepository.findAll(pageRequest)
        );
    }

    @Override
    public CategoryDto getCategory(long id) {
        log.debug("Запрос getCategories по id - {}", id);

        return CategoryMapper.toCategoryDto(
                categoryRepository.checkAndReturnCategoryIfExist(id)
        );
    }
}
