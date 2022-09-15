package ru.practicum.diplom.admin.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto SaveCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.saveCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto UpdateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }
}
