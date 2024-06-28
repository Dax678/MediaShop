package org.example.mediashop.Repository;

import org.example.mediashop.Data.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryById(long id);

    Optional<Category> findCategoryByTitle(String title);
}
