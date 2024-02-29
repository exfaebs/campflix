package net.ictcampus.campflix.controller.repositories;

import net.ictcampus.campflix.model.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    @Query("SELECT m FROM Movie m WHERE m.name LIKE CONCAT('%', :name, '%')")
    Iterable<Movie> findByName(@Param("name") String name);

    @Query("SELECT m FROM Movie m JOIN m.genre gen WHERE gen.name LIKE CONCAT ('%', :name, '%')")
    Iterable<Movie> findByGenreName(@Param("name") String name);
}