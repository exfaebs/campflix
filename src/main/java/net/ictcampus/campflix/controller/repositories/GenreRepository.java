package net.ictcampus.campflix.controller.repositories;

import net.ictcampus.campflix.model.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {

    @Query("SELECT g FROM Genre g WHERE g.name LIKE CONCAT('%', :name, '%')")
    Iterable<Genre> findByName(@Param("name") String name);

}