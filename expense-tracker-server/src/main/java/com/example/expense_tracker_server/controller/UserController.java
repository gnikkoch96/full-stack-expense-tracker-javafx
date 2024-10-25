package com.example.expense_tracker_server.controller;

import com.example.expense_tracker_server.entity.User;
import com.example.expense_tracker_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    // creating a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam String name, @RequestParam String email,
                                            @RequestParam String password){
        // hash the password before saving it
        String hashedPassword = passwordEncoder.encode(password);

        User newUser = userService.createUser(name, email, hashedPassword);
        return ResponseEntity.ok(newUser);
    }

    // authenticate user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password){
        // get the user by email
        Optional<User> user = userService.getUserByEmail(email);

        if(user.isEmpty() || user == null){
            // could not find email, return error
            return ResponseEntity.status(401).body("Invalid Email");
        }

        // check if the entered password matches
        boolean isAuthenticated = passwordEncoder.matches(password, user.get().getPassword());

        if(isAuthenticated){
            return ResponseEntity.status(200).body("Login Successful!");
        }else{
            return ResponseEntity.status(401).body("Invalid Password");
        }
    }

    // delete user by email
    @DeleteMapping
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.status(204).body("Account has been deleted.");
    }

}


























