package net.ictcampus.campflix.controller.services;

import net.ictcampus.campflix.controller.repositories.GenreRepository;
import net.ictcampus.campflix.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Iterable<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findById(Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElseThrow(EntityNotFoundException::new);
    }

    public Iterable<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    public void insert(Genre newGenre) {
        genreRepository.save(newGenre);
    }

    public void update(Genre genre) {
        genreRepository.save(genre);
    }

    public void deleteById(Integer id) {
        genreRepository.deleteById(id);
    }
}