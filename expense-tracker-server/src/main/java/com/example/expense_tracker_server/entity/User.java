package com.example.expense_tracker_server.entity;

import jakarta.persistence.*;

@Entity // marks class as JPA entity which means that it will be mapped to the user table in our db
@Table(name = "users") // labels this entity to the proper name which is "users"
public class User {
    @Id // marks this variable as the unique id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // indicates that the database will handle the ID generation
    private int id;

    private String name;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
