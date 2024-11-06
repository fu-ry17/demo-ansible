package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.todo.TodoClient;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import com.turnkey.turnquest.gis.quotation.enums.LinkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DealTodoServiceImplTest {

    @Mock
    private TodoClient todoClient;

    private DealTodoServiceImpl dealTodoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dealTodoService = new DealTodoServiceImpl(todoClient);
    }

    @Test
    void shouldSaveAllTodos() {
        TodoDto todo1 = new TodoDto();
        TodoDto todo2 = new TodoDto();
        List<TodoDto> todos = Arrays.asList(todo1, todo2);
        when(todoClient.saveTodo(any(TodoDto.class))).thenAnswer(i -> i.getArgument(0));

        List<TodoDto> result = dealTodoService.saveAllTodo(todos);

        assertEquals(2, result.size());
        verify(todoClient, times(2)).saveTodo(any(TodoDto.class));
    }


    @Test
    void shouldSaveTodoWithLink() {
        TodoDto todo = new TodoDto();
        todo.setDealId(1L);
        when(todoClient.saveTodo(any(TodoDto.class))).thenAnswer(i -> i.getArgument(0));

        TodoDto result = dealTodoService.save(todo);

        assertEquals(1, result.getTodoLinks().size());
        assertEquals(LinkType.DEAL, result.getTodoLinks().get(0).getLinkType());
        assertEquals(1L, result.getTodoLinks().get(0).getLinkId());
        verify(todoClient, times(1)).saveTodo(any(TodoDto.class));
    }

    @Test
    void shouldFindAllTodos() {
        TodoDto todo1 = new TodoDto();
        TodoDto todo2 = new TodoDto();
        List<TodoDto> todos = Arrays.asList(todo1, todo2);
        when(todoClient.getAllTodos()).thenReturn(todos);

        List<TodoDto> result = dealTodoService.findAll();

        assertEquals(2, result.size());
        verify(todoClient, times(1)).getAllTodos();
    }
}
