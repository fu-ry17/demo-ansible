package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;

import java.util.List;

public interface DealTodoService {

    TodoDto save(TodoDto todo);

    List<TodoDto> findAll();

    List<TodoDto> saveAllTodo(List<TodoDto> todos);

}
