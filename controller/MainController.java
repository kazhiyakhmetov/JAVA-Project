package com.example.hardlab5.controller;


import com.example.hardlab5.model.User;
import com.example.hardlab5.model.Category;
import com.example.hardlab5.model.Task;
import com.example.hardlab5.next.CategoryService;
import com.example.hardlab5.next.TaskService;
import com.example.hardlab5.next.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CategoryService categoryService;

    // Страница регистрации
    @GetMapping("/auth/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());  // Используем вашу сущность User
        return "register";  // Страница с формой регистрации
    }

    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Сохраняем пользователя в базе данных
        userService.saveUser(user);
        return "redirect:/auth/login";  // После регистрации перенаправляем на страницу логина
    }

    // Страница логина
    @GetMapping("/auth/login")
    public String showLoginForm() {
        return "login";  // Страница с формой логина
    }


    // Главная страница
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal org.springframework.security.core.userdetails.User currentUser, Model model) {
        Long userId = userService.findByUsername(currentUser.getUsername()).getId();
        List<Task> tasks = taskService.findTasksByUser(userId);
        model.addAttribute("tasks", tasks);
        return "home";  // Страница с задачами пользователя
    }

    // Страница категорий
    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";  // Страница с категориями задач
    }

    // Страница задач по категории
    @GetMapping("/tasks/{categoryId}")
    public String tasks(@PathVariable Long categoryId, Model model) {
        List<Task> tasks = taskService.findTasksByCategory(categoryId);
        model.addAttribute("tasks", tasks);
        return "tasks";  // Страница с задачами для выбранной категории
    }

    // Страница для добавления новой задачи
    @GetMapping("/task/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "new-task";  // Страница с формой для добавления задачи
    }

    // Обработка POST запроса для добавления задачи
    @PostMapping("/task")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal User currentUser) {
        task.setUser(userService.findByUsername(currentUser.getUsername()));  // Связываем задачу с текущим пользователем
        taskService.addTask(task);  // Добавляем задачу
        return "redirect:/home";  // Перенаправляем на главную страницу после добавления задачи
    }
}
