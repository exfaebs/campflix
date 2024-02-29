package net.ictcampus.campflix.controller.services;

import net.ictcampus.campflix.controller.repositories.MovieRepository;
import net.ictcampus.campflix.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie findById(Integer id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElseThrow(EntityNotFoundException::new);
    }

    public Iterable<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Iterable<Movie> findByGenreName(String genreName) {
        return movieRepository.findByGenreName(genreName);
    }

    public Iterable<Movie> findByName(String name) {
        return movieRepository.findByName(name);
    }

    public void insert(Movie newMovie) {
        movieRepository.save(newMovie);
    }

    public void update(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteById(Integer id) {
        movieRepository.deleteById(id);
    }

}