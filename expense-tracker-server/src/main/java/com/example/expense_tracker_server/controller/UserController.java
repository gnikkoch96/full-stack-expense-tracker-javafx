package com.example.expense_tracker_server.controller;

import com.example.expense_tracker_server.entity.User;
import com.example.expense_tracker_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    // get user
    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email){
        logger.info("Get user by email: " + email);

        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());  // if the user exists, return the user
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // if the user doesn't exist, return 404
        }
    }

    // creating a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        logger.info("Creating New User");

        // hash the password before saving it
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = userService.createUser(user.getName(), user.getEmail(), hashedPassword);
        return ResponseEntity.ok(newUser);
    }

    // authenticate user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password){
        // get the user by email
        Optional<User> user = userService.getUserByEmail(email);

        if(user.isEmpty()){
            // could not find email, return error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email is not registered into our database");
        }

        // check if the entered password matches
        boolean isAuthenticated = passwordEncoder.matches(password, user.get().getPassword());

        if(isAuthenticated){
            return ResponseEntity.status(HttpStatus.OK).body(user.get().getEmail());
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }
    }

    // delete user by email
    @DeleteMapping
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account has been deleted.");
    }

}


























