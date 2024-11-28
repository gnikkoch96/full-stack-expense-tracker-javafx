package org.example.models;

public class TransactionCategory {
    private int id;
    private User user;
    private String categoryName;
    private String categoryColor;

    public TransactionCategory(int id, User user, String categoryName, String categoryColor) {
        this.id = id;
        this.user = user;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public int getId() {
        return id;
    }

    public User getUser(){
        return user;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}
