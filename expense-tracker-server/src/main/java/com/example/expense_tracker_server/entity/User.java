package com.example.expense_tracker_server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity // marks class as JPA entity which means that it will be mapped to the user table in our db
@Table(name = "user") // labels this entity to the proper name which is "users"
public class User {
    @Id // marks this variable as the unique id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // indicates that the database will handle the ID generation
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public int getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
