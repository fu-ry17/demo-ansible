package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.DealTodoController;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import com.turnkey.turnquest.gis.quotation.service.DealTodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class DealTodoControllerTest {


    @Mock
    private DealTodoService dealTodoService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DealTodoController dealTodoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAllDealTodos() {
        TodoDto todoDto1 = new TodoDto();
        TodoDto todoDto2 = new TodoDto();
        List<TodoDto> todos = Arrays.asList(todoDto1, todoDto2);

        when(dealTodoService.saveAllTodo(todos)).thenReturn(todos);

        ResponseEntity<List<TodoDto>> response = dealTodoController.saveAllDealTodos(todos, authentication);

        assertEquals(todos, response.getBody());
    }

    @Test
    void shouldSaveDealTodo() {
        TodoDto todoDto = new TodoDto();

        when(dealTodoService.save(todoDto)).thenReturn(todoDto);

        ResponseEntity<TodoDto> response = dealTodoController.saveDealTodo(todoDto, authentication);

        assertEquals(todoDto, response.getBody());
    }

    @Test
    void shouldGetAllDealTodos() {
        TodoDto todoDto1 = new TodoDto();
        TodoDto todoDto2 = new TodoDto();
        List<TodoDto> todos = Arrays.asList(todoDto1, todoDto2);

        when(dealTodoService.findAll()).thenReturn(todos);

        ResponseEntity<List<TodoDto>> response = dealTodoController.getAllDealTodos(authentication);

        assertEquals(todos, response.getBody());
    }

}
