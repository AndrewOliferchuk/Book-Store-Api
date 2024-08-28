package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookResponseDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CreateBookRequestDto requestDto;
    private BookResponseDto responseDto;
    @MockBean
    private BookService bookService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create book")
    @Sql(scripts = "classpath:database/books/delete-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void createBook_ValidRequest_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Jack London");
        requestDto.setTitle("The Iron Heel");
        requestDto.setPrice(BigDecimal.valueOf(34));
        requestDto.setIsbn("978-3-16-148410-0");

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(1L);
        responseDto.setAuthor(requestDto.getAuthor());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setIsbn(requestDto.getIsbn());
        responseDto.setPrice(requestDto.getPrice());

        given(bookService.save(any(CreateBookRequestDto.class))).willReturn(responseDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getTitle(), responseDto.getTitle());
        Assertions.assertEquals(actual.getAuthor(), responseDto.getAuthor());
        Assertions.assertEquals(actual.getIsbn(), responseDto.getIsbn());
        Assertions.assertEquals(actual.getPrice(), responseDto.getPrice());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/books/delete-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getBookById_ValidBookId_Success() throws Exception {
        Long bookId = 1L;

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(bookId);
        responseDto.setAuthor("Jack London");
        responseDto.setTitle("The Iron Heel");
        responseDto.setPrice(BigDecimal.valueOf(34));
        responseDto.setIsbn("978-3-16-148410-0");

        when(bookService.getBookById(bookId)).thenReturn(responseDto);

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        Assertions.assertEquals(actual.getPrice(), responseDto.getPrice());
        Assertions.assertEquals(actual.getIsbn(), responseDto.getIsbn());
        Assertions.assertEquals(actual.getAuthor(), responseDto.getAuthor());
        Assertions.assertEquals(actual.getTitle(), responseDto.getTitle());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void delete_ValidBookId_Success() throws Exception {
        Long bookId = 1L;

        Mockito.doNothing().when(bookService).deleteById(bookId);

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService, Mockito.times(
                1)).deleteById(bookId);
    }
}
