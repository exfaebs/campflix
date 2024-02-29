package net.ictcampus.campflix.controller.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ictcampus.campflix.controller.services.MovieService;
import net.ictcampus.campflix.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "Find all movies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movies found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movies not found", content = @Content) })
    public Iterable<Movie> findByNameAndGenreName(@RequestParam(required = false) String name,
            @RequestParam(required = false) String genreName) {
        try {
            if (name != null) {
                return movieService.findByName(name);
            } else if (genreName != null) {
                return movieService.findByGenreName(genreName);
            } else {
                return movieService.findAll();
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
    }

    @GetMapping(path = "{id}")
    @Operation(summary = "Find a movie by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content) })
    public Movie findById(@PathVariable Integer id) {
        try {
            return movieService.findById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    @Operation(summary = "Create a new movie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie was created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "409", description = "Movie could not be created", content = @Content) })
    public void insert(@Valid @RequestBody Movie newMovie) {
        try {
            movieService.insert(newMovie);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, " Movie could not be added");
        }
    }

    @PutMapping(consumes = "application/json")
    @Operation(summary = "Update a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie was updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "409", description = "Movie could not be updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }) })
    public void update(@Valid @RequestBody Movie movie) {
        try {
            movieService.update(movie);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie could not be updated");
        }
    }

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Delete a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie was deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie could not be deleted", content = @Content) })
    public void delete(@PathVariable Integer id) {
        try {
            movieService.deleteById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie could not be deleted");
        }
    }
}