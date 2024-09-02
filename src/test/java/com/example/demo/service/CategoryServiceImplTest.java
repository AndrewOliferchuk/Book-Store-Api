package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void save_ValidCreateDto_returnResponseDto() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Old");
        category.setDescription("Classic");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(categoryDto);

        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryMapper).toModel(categoryDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void findAll_validPageable_returnAllCategories() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fear");
        category1.setDescription("very terrible");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("New");
        category2.setDescription("Only new");

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(category1.getId());
        categoryDto1.setName(category1.getName());
        categoryDto1.setDescription(category1.getDescription());

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(category2.getId());
        categoryDto2.setName(category2.getName());
        categoryDto2.setDescription(category2.getDescription());

        List<CategoryDto> categoryDtos = List.of(categoryDto1,categoryDto2);
        List<Category> allCategory = List.of(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(allCategory);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        Pageable pageable = PageRequest.of(0, 10);
        List<CategoryDto> repository = categoryService.findAll(pageable);

        assertThat(repository.size()).isEqualTo(2);
        assertThat(repository).isEqualTo(categoryDtos);
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category1);
        verify(categoryMapper).toDto(category2);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    void getById_validCategoryDto_Success() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Old");
        category.setDescription("Classic");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(categoryId);

        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryMapper).toDto(category);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Test with empty id")
    void getCategoryById_emptyId_ThrowsEntityNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        });

        assertEquals("Category not found by id: " + categoryId, exception.getMessage());
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
