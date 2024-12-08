package com.example.hardlab5.repo;

import com.example.hardlab5.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // это чтлобы получить задачи пользователя
    List<Task> findByUserId(Long userId);

    // это чтобы олучить задачи по категории
    List<Task> findByCategoryId(Long categoryId);

    // это поиск задач по названию
    List<Task> findByTitleContainingIgnoreCase(String title);

    // это пагинация задач пользователя
    Page<Task> findByUserId(Long userId, Pageable pageable);

    // это пагинация с поиском по названию
    Page<Task> findByUserIdAndTitleContaining(Long userId, String title, Pageable pageable);

    // это пагинация с фильтром по категории и поисковому запросу
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.category.name = :category AND (:query IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Task> findByUserAndCategory(
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("query") String query,
            Pageable pageable
    );

    Page<Task> findByUserIdAndTitleContainingAndCategoryName(Long userId, String title, String category, Pageable pageable);

    Page<Task> findByUserIdAndCategoryName(Long userId, String category, Pageable pageable);

}
