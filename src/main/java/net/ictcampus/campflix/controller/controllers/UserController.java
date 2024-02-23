package net.ictcampus.campflix.controller.controllers;

import net.ictcampus.campflix.controller.services.UserService;
import net.ictcampus.campflix.model.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/users") //nimmt alle /users request an
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "{id}")
    public User findById(@PathVariable Integer id) { //wichtig dass es Integer ist und nicht int
        try{
            return userService.findById(id);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
    @GetMapping
    public Iterable<User> findByName(@RequestParam(required = false) String name) {

        try{
            if (name != null){
                return userService.findByName(name);

            } else{
                return userService.findAll();
            }

        }

        catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody User user){
        try{
            System.out.println(user.getUsername()); //todo remove after debug
            userService.signUp(user);
        } catch(RuntimeException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not register");
        }
    }

    @DeleteMapping(path = "{id}")
    public void deleteById(@PathVariable Integer id){
        try{
            User user = userService.findById(id);
            userService.delete(user);
        } catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }

    @PutMapping(consumes = "application/json")
    public void update(@Valid @RequestBody User user) {
        try{
            userService.update(user);
        } catch(RuntimeException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update");
        }
    }
}
