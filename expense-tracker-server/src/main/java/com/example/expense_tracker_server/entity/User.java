package com.example.expense_tracker_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // marks class as JPA entity which means that it will be mapped to the user table in our db
public class User {
    @Id // marks this variable as the unique id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private int id;

    private String name;
    private String email;

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
}
