package nl.abnamro.assignment.recipe.repository;

import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findById(Long id);
    Optional<RecipeEntity> findByNameAndUserId(String name, Long userId);
    List<RecipeEntity> findByUserId(Pageable pageable, Long userId);
    Optional<RecipeEntity> findByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);

}
