package net.ictcampus.campflix.controller.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ictcampus.campflix.controller.services.GenreService;
import net.ictcampus.campflix.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    @Operation(summary = "Find all genres.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }),
            @ApiResponse(responseCode = "404", description = "Genres not found", content = @Content) })
    public Iterable<Genre> findByName(@RequestParam(value = "name", required = false) String name) {
        try {
            if (name != null) {
                return genreService.findByName(name);
            } else {
                return genreService.findAll();
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found");
        }
    }

    @GetMapping(path = "{id}")
    @Operation(summary = "Find a genre by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }),
            @ApiResponse(responseCode = "404", description = "Genre not found", content = @Content) })
    public Genre findById(@PathVariable Integer id) {
        try {
            return genreService.findById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    @Operation(summary = "Create a new genre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre was created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }),
            @ApiResponse(responseCode = "409", description = "Genre could not be created", content = @Content) })
    public void insert(@Valid @RequestBody Genre newGenre) {
        try {
            genreService.insert(newGenre);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre could not be added");
        }
    }

    @PutMapping(consumes = "application/json")
    @Operation(summary = "Update an existing genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre was updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }),
            @ApiResponse(responseCode = "409", description = "Genre could not be updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }) })
    public void update(@Valid @RequestBody Genre genre) {
        try {
            genreService.update(genre);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre could not be updated");
        }
    }

    @DeleteMapping(path = "{id}")
    @Operation(summary = "Delete a genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre was deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Genre.class)) }),
            @ApiResponse(responseCode = "404", description = "Genre could not be deleted", content = @Content) })
    public void delete(@PathVariable Integer id) {
        try {
            genreService.deleteById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre could not be deleted");
        }
    }
}