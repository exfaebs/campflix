package net.ictcampus.campflix.controller.services;

import net.ictcampus.campflix.controller.repositories.UserRepository;
import net.ictcampus.campflix.model.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Integer id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(EntityNotFoundException::new); //Wenn es kein User-Objekt gibt, soll es diesen Fehler ausgeben
    }

    public Iterable<User> findByName(String query){
        Iterable<User> userIterable = userRepository.findByUsername(query);
        return userIterable;
    }

    public Iterable<User> findAll(){
        Iterable<User> userIterable = userRepository.findAll();
        return userIterable;
    }

//    public void insert(String username, String password){
//        User newUser = new User();
//        newUser.setPassword(password);
//        newUser.setUsername(username);
//        userRepository.save(newUser);
//    }
    public void signUp(User user){

        userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void update(User user) {userRepository.save(user);}


}
