//package com.example.hardlab5.next;
//
//
//import com.example.hardlab5.model.Category;
//import com.example.hardlab5.repo.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CategoryService {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    public Category getCategoryById(Long id) {
//        return categoryRepository.findById(id).orElse(null);
//    }
//
//    public Category addCategory(Category category) {
//        return categoryRepository.save(category);
//    }
//
//    public void deleteCategory(Long categoryId) {
//        categoryRepository.deleteById(categoryId);
//    }
//}

package com.example.hardlab5.next;

import com.example.hardlab5.model.Category;
import com.example.hardlab5.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }


    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Category updateCategory(Long categoryId, Category updatedCategory) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryId);
        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }
}



