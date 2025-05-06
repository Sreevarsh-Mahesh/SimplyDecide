package main.model;

import java.sql.Timestamp;

public class Decision {
    private int id;
    private String title;
    private Timestamp createdAt;

    public Decision(int id, String title, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public Decision(String title) {
        this(-1, title, new Timestamp(System.currentTimeMillis()));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
} 