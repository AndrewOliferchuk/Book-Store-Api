package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookResponseDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exeption.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import java.math.BigDecimal;
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
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify create, method works")
    void save_ValidCreateRequestDto_returnResponseDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Jack London");
        requestDto.setTitle("The Iron Heel");
        requestDto.setPrice(BigDecimal.valueOf(34));
        requestDto.setIsbn("qrer");

        Book book = new Book();
        book.setAuthor(requestDto.getAuthor());
        book.setTitle(requestDto.getTitle());
        book.setPrice(requestDto.getPrice());
        book.setIsbn(requestDto.getIsbn());

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(1L);
        responseDto.setAuthor(book.getAuthor());
        responseDto.setTitle(book.getTitle());
        responseDto.setPrice(book.getPrice());
        responseDto.setIsbn(book.getIsbn());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(responseDto);

        BookResponseDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(responseDto);
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void findAll_ValidPageable_returnAllBook() {
        Book book = new Book();
        book.setAuthor("Jack London");
        book.setTitle("The Iron Heel");
        book.setPrice(BigDecimal.valueOf(56));
        book.setIsbn("gregher");

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(1L);
        responseDto.setAuthor(book.getAuthor());
        responseDto.setTitle(book.getTitle());
        responseDto.setPrice(book.getPrice());
        responseDto.setIsbn(book.getIsbn());

        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books);

        when(bookMapper.toDto(book)).thenReturn(responseDto);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Pageable pageable = PageRequest.of(0, 10);
        List<BookResponseDto> actual = bookService.findAll(pageable);

        assertThat(actual.get(0)).isEqualTo(responseDto);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void getBookById_ValidFoundById_returnBookResponseDto() {
        Book book = new Book();
        book.setAuthor("Jack London");
        book.setTitle("The Iron Heel");
        book.setPrice(BigDecimal.valueOf(56));
        book.setIsbn("gregher");

        Long bookId = 1L;
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(bookId);
        responseDto.setAuthor(book.getAuthor());
        responseDto.setTitle(book.getTitle());
        responseDto.setPrice(book.getPrice());
        responseDto.setIsbn(book.getIsbn());

        when(bookMapper.toDto(book)).thenReturn(responseDto);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookResponseDto actual = bookService.getBookById(bookId);

        assertThat(actual).isEqualTo(responseDto);
        verify(bookMapper).toDto(book);
        verify(bookRepository).findById(responseDto.getId());
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Test with empty id")
    void getBookById_InvalidId_ThrowsEntityNotFoundException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookById(bookId);
        });

        assertEquals("Can't get book by id:" + bookId, exception.getMessage());
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void deleteById_mustDelete_WhenIdExist() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
