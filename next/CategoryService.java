package com.example.hardlab5.next;


import com.example.hardlab5.model.Category;
import com.example.hardlab5.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Метод для получения всех категорий
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Метод для получения категории по ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Метод для добавления новой категории
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Метод для удаления категории
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
