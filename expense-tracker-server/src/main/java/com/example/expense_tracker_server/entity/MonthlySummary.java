package com.example.expense_tracker_server.entity;

import jakarta.persistence.*;

@Entity
@Table(name="monthly_summary")
public class MonthlySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalIncome;
    private double totalExpense;

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
