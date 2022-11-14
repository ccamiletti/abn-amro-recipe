package nl.abnamro.assignment.recipe.repository;

import nl.abnamro.assignment.recipe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByUserName(String userName);



}
