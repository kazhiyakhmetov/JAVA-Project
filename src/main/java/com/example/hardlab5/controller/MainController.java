package com.example.hardlab5.controller;

import com.example.hardlab5.model.User;
import com.example.hardlab5.model.Category;
import com.example.hardlab5.model.Task;
import com.example.hardlab5.next.CategoryService;
import com.example.hardlab5.next.EmailService;
import com.example.hardlab5.next.TaskService;
import com.example.hardlab5.next.UserService;
import com.example.hardlab5.repo.TaskRepository;
import com.example.hardlab5.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    private final UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;


    @GetMapping("/auth/register")
    public String showRegisterForm(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("errorMessage", "Email уже есть ");
        }
        return "register";
    }

    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute("user") User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return "redirect:/auth/register?error=email_exists";
        }
        userService.saveUser(user);
        return "redirect:/auth/login";
    }


    @GetMapping("/auth/login")
    public String showLoginForm() {
        return "login";
    }



    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails currentUser,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "query", required = false) String query,
                       @RequestParam(value = "category", required = false) String category,
                       Model model) {
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        String username = currentUser.getUsername();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", user);

        try {
            PageRequest pageRequest = PageRequest.of(page, 3);

            Page<Task> tasksPage;

            if ((query != null && !query.isEmpty()) && (category != null && !category.isEmpty())) {
                tasksPage = taskService.findTasksByUserAndTitleAndCategory(user.getId(), query, category, pageRequest);
            } else if (query != null && !query.isEmpty()) {
                tasksPage = taskService.findTasksByUserAndTitleContaining(user.getId(), query, pageRequest);
            } else if (category != null && !category.isEmpty()) {
                tasksPage = taskService.findTasksByUserAndCategory(user.getId(), category, pageRequest);
            } else {
                tasksPage = taskService.findTasksByUser(user.getId(), pageRequest);
            }

            model.addAttribute("tasksPage", tasksPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", tasksPage.getTotalPages());
            model.addAttribute("query", query);
            model.addAttribute("category", category);

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);

        } catch (Exception e) {
            System.err.println("Ошибка " + e.getMessage());
            model.addAttribute("errorMessage", "ошибка ");
            return "error-page";
        }

        return "home";
    }


    @Autowired
    private EmailService emailService;


    @PostMapping("/home/send-email")
    public String sendEmail(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("message") String message,
            Model model) {
        try {
            emailService.sendEmail(email, name, message);
            model.addAttribute("successMessage", "Письмо отправлено");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при отправке письма " + e.getMessage());
        }
        return "redirect:/home";
    }


    @Autowired
    public MainController(TaskRepository taskRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/search")
    public String searchTasks(@RequestParam("query") String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            model.addAttribute("tasks", taskRepository.findAll());
        } else {
            model.addAttribute("tasks", taskRepository.findByTitleContainingIgnoreCase(query.trim()));
        }
        return "home";
    }




    private final String uploadDirectory = "C:/Users/home/IdeaProjects/hardlab5/src/main/resources/images/";


    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Юзер не найден");
        }
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal UserDetails currentUser) {
        if (file.isEmpty()) {
            return "redirect:/profile?error=empty_file";
        }

        User user = userService.findByUsername(currentUser.getUsername());

        if (user != null) {
            try {
                String filename = user.getUsername() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDirectory + filename);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                user.setAvatarPath("/images/" + filename);
                String currentPassword = user.getPassword();
                user.setPassword(currentPassword);

                userService.saveUser(user);

            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/profile?error=upload_failed";
            }
        }

        return "redirect:/profile";
    }


    @PostMapping("/profile/delete-avatar")
    public String deleteAvatar(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        if (user != null) {
            user.setAvatarPath(null);
            userService.saveUser(user);
        }
        return "redirect:/profile";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/tasks/{categoryId}")
    public String tasks(@PathVariable Long categoryId, Model model) {
        List<Task> tasks = taskService.findTasksByCategory(categoryId);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/task/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("statuses", Task.Status.values());
        model.addAttribute("priorities", Task.Priority.values());
        return "new-task";
    }



    @PostMapping("/task")
    public String addTask(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("dueDate") String dueDate,
            @RequestParam(value = "status", required = false, defaultValue = "PENDING") String status,
            @RequestParam("priority") String priority,
            @RequestParam("categoryId") Long categoryId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        User user = userService.findByUsername(currentUser.getUsername());
        Category category = categoryService.getCategoryById(categoryId);

        if (user != null && category != null) {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate.isEmpty() ? null : LocalDate.parse(dueDate));
            task.setStatus(Task.Status.valueOf(status.toUpperCase()));
            task.setPriority(Task.Priority.valueOf(priority.toUpperCase()));
            task.setCategory(category);
            task.setUser(user);

            taskService.addTask(task);
        }

        return "redirect:/home";
    }


    @GetMapping("/task/{id}")
    public String viewTaskDetails(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return "redirect:/home";
        }
        model.addAttribute("task", task);
        return "task-details";
    }



    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return "redirect:/home";
    }
}