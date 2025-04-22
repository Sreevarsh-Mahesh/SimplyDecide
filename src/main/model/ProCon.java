package main.model;

public class ProCon {
    public enum Type {
        PRO,
        CON
    }

    private int id;
    private int choiceId;
    private Type type;
    private String content;

    public ProCon(int id, int choiceId, Type type, String content) {
        this.id = id;
        this.choiceId = choiceId;
        this.type = type;
        this.content = content;
    }

    public ProCon(int choiceId, Type type, String content) {
        this(-1, choiceId, type, content);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getChoiceId() { return choiceId; }
    public void setChoiceId(int choiceId) { this.choiceId = choiceId; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
} 