package main.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Choice {
    private int id;
    private String title;
    private Timestamp createdAt;
    private List<ProCon> prosAndCons;
    private int decisionId;

    public Choice(int id, String title, Timestamp createdAt, int decisionId) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.decisionId = decisionId;
        this.prosAndCons = new ArrayList<>();
    }

    public Choice(String title, int decisionId) {
        this(-1, title, new Timestamp(System.currentTimeMillis()), decisionId);
    }

    // For backward compatibility
    public Choice(int id, String title, Timestamp createdAt) {
        this(id, title, createdAt, -1);
    }

    public Choice(String title) {
        this(-1, title, new Timestamp(System.currentTimeMillis()), -1);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Timestamp getCreatedAt() { return createdAt; }
    public List<ProCon> getProsAndCons() { return prosAndCons; }
    public void setProsAndCons(List<ProCon> prosAndCons) { this.prosAndCons = prosAndCons; }
    public int getDecisionId() { return decisionId; }
    public void setDecisionId(int decisionId) { this.decisionId = decisionId; }

    public void addProCon(ProCon proCon) {
        prosAndCons.add(proCon);
    }

    public List<ProCon> getPros() {
        return prosAndCons.stream()
            .filter(pc -> pc.getType() == ProCon.Type.PRO)
            .toList();
    }

    public List<ProCon> getCons() {
        return prosAndCons.stream()
            .filter(pc -> pc.getType() == ProCon.Type.CON)
            .toList();
    }
} 