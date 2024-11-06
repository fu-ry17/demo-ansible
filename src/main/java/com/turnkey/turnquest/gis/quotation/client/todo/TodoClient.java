package com.turnkey.turnquest.gis.quotation.client.todo;

import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("todo-service")
public interface TodoClient {

    @PostMapping("todos")
    TodoDto saveTodo(@RequestBody TodoDto todo);

    @GetMapping("todos")
    List<TodoDto> getAllTodos();

    @GetMapping("todos/{dealId}/deal")
    List<TodoDto> findTodoByDealId(@PathVariable Long dealId);
}
