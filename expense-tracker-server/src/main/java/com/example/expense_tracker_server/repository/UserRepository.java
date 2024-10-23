package com.example.expense_tracker_server.repository;

import com.example.expense_tracker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// allows us to easily perform CRUD operations to User data in our database
public interface UserRepository extends JpaRepository<User, Integer> {
}
