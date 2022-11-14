package nl.abnamro.assignment.recipe.repository;

import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface RecipeRepository extends ReactiveCrudRepository<RecipeEntity, Long> {

    @Query("SELECT * FROM recipe WHERE id = :id")
    Mono<RecipeEntity> findById(Long id);

    @Query("SELECT * FROM recipe WHERE name = :name and user_id = :userId")
    Optional<RecipeEntity> findByNameAndUserId(String name, Long userId);

    @Query("SELECT * FROM recipe WHERE user_id = :userId")
    Flux<RecipeEntity> findByUserId(Long userId);

    @Query("SELECT * FROM recipe WHERE id = id and user_id = :userId")
    Optional<RecipeEntity> findByIdAndUserId(Long id, Long userId);


    //void deleteByIdAndUserId(Long id, Long userId);

}
