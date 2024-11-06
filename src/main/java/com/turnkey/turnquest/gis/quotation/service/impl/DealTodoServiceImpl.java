package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.todo.TodoClient;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoLinkDto;
import com.turnkey.turnquest.gis.quotation.enums.LinkType;
import com.turnkey.turnquest.gis.quotation.service.DealTodoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("dealTodoService")
public class DealTodoServiceImpl implements DealTodoService {

    private final TodoClient todoClient;

    public DealTodoServiceImpl(TodoClient todoClient) {
        this.todoClient = todoClient;
    }

    @Override
    public List<TodoDto> saveAllTodo(List<TodoDto> todos) {
        return todos.stream().peek(todoClient::saveTodo).collect(Collectors.toList());

    }

    @Override
    public TodoDto save(TodoDto todo) {
        TodoLinkDto todoLink = new TodoLinkDto(LinkType.DEAL, todo.getDealId());
        todo.setTodoLinks(Collections.singletonList(todoLink));
        return todoClient.saveTodo(todo);
    }

    @Override
    public List<TodoDto> findAll() {
        return todoClient.getAllTodos();
    }
}
