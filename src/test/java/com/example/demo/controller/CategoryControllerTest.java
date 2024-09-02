package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CategoryDto categoryDto;
    private CategoryRequestDto categoryRequestDto;
    @MockBean
    private CategoryService categoryService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create category")
    @Sql(scripts = "classpath:database/category/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void createCategory_ValidRequest_Success() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Sale");
        categoryRequestDto.setDescription("Have a discount");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        categoryRequestDto.setName(categoryRequestDto.getName());
        responseDto.setDescription(categoryRequestDto.getDescription());

        given(categoryService.save(any(CategoryDto.class))).willReturn(responseDto);

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/category")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getName(), responseDto.getName());
        Assertions.assertEquals(actual.getDescription(), responseDto.getDescription());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/category/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getCategoryById_ValidCategoryId_Success() throws Exception {
        Long categoryId = 1L;

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(categoryId);
        responseDto.setName("Sale");
        responseDto.setDescription("Have a discount");

        when(categoryService.getById(categoryId)).thenReturn(responseDto);

        MvcResult result = mockMvc.perform(get("/category/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertEquals(actual.getName(), responseDto.getName());
        Assertions.assertEquals(actual.getDescription(), responseDto.getDescription());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteCategory_ValidCategoryId_Success() throws Exception {
        Long categoryId = 1L;

        Mockito.doNothing().when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService, Mockito.times(
                1)).deleteById(categoryId);
    }
}
