package com.example.hardlab5.next;



import com.example.hardlab5.model.Task;
import com.example.hardlab5.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Получение задач пользователя
    public List<Task> findTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    // Получение задач по категории
    public List<Task> findTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }

    // Добавление новой задачи
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }
}
