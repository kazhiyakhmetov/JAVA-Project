//package com.example.hardlab5.next;
//
//
//
//import com.example.hardlab5.model.Task;
//import com.example.hardlab5.repo.TaskRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TaskService {
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    public List<Task> findTasksByUser(Long userId) {
//        return taskRepository.findByUserId(userId);
//    }
//
//    public List<Task> findTasksByCategory(Long categoryId) {
//        return taskRepository.findByCategoryId(categoryId);
//    }
//
//    public Task addTask(Task task) {
//        return taskRepository.save(task);
//    }
//}
package com.example.hardlab5.next;

import com.example.hardlab5.model.Task;
import com.example.hardlab5.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public void addTask(Task task) {
        taskRepository.save(task); // Сохраняет задачу в базу данных
    }


    public Page<Task> findTasksByUser(Long userId, PageRequest pageRequest) {
        return taskRepository.findByUserId(userId, pageRequest);
    }

    public Page<Task> findTasksByUserAndTitleContaining(Long userId, String query, Pageable pageable) {
        return taskRepository.findByUserIdAndTitleContaining(userId, query, pageable);
    }


    public List<Task> findTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }



    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setCategory(updatedTask.getCategory());
            return taskRepository.save(existingTask);
        }
        return null;
    }

    public Optional<Task> findTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}
