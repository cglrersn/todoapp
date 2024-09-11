package com.caglar.todoapp.repository;

import com.caglar.todoapp.entity.TodoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<TodoItem, Long> {

    Optional<TodoItem> findByDescriptionAndUserId(String description, Long userId);

    List<TodoItem> findByUserId(Long userId);
}
