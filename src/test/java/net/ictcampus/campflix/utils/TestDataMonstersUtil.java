package net.ictcampus.campflix.utils;

import net.ictcampus.campflix.model.Genre;
import net.ictcampus.campflix.model.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestDataMonstersUtil {
    public static List<Movie> getTestMovies(){
        List<Movie> movies = new ArrayList<>();
        HashSet<Genre> genres = new HashSet<>();

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Melodrama");
        genres.add(genre);

        for (int i = 1; i <= 3; i++) {
            Movie movie = new Movie();
            movie.setId(i);
            movie.setName("Movie"+i);
            movie.setDuration(120);
            genre.getMovies().add(movie);
            movie.setGenre(genre);
            movies.add(movie);
        }

        return movies;
    }
}
