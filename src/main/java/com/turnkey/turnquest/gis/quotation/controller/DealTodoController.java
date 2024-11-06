package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import com.turnkey.turnquest.gis.quotation.service.DealTodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/deal-todos")
public class DealTodoController {

    private final DealTodoService dealTodoService;
    private final TokenUtils tokenUtils;

    public DealTodoController(DealTodoService dealTodoService, TokenUtils tokenUtils) {
        this.dealTodoService = dealTodoService;
        this.tokenUtils = tokenUtils;
    }

    @RolesAllowed({"quot_deal_todo_save_all","agent"})
    @PostMapping("/all")
    public ResponseEntity<List<TodoDto>> saveAllDealTodos(@RequestBody List<TodoDto> todos, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(dealTodoService.saveAllTodo(todos));
    }

    @RolesAllowed({"quot_deal_todo_save","agent"})
    @PostMapping
    public ResponseEntity<TodoDto> saveDealTodo(@RequestBody TodoDto todoDto, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(dealTodoService.save(todoDto));
    }

    @RolesAllowed({"quot_deal_todo_get_all","agent"})
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllDealTodos(Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(dealTodoService.findAll());
    }

}
