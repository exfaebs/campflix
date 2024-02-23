package net.ictcampus.campflix.controller.repositories;

import net.ictcampus.campflix.model.models.Movie;
import net.ictcampus.campflix.model.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT ('%', :name, '%')")
    Iterable<User> findByUsername(@Param("name") String name);

}

