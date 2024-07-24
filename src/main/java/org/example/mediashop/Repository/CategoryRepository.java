package org.example.mediashop.Repository;

import jakarta.transaction.Transactional;
import org.example.mediashop.Data.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE category CASCADE", nativeQuery = true)
    void deleteAll();

    Optional<Category> findCategoryById(long id);

    Optional<Category> findCategoryByTitle(String title);

    List<Category> findCategoriesByParentId(long parentId);
}
