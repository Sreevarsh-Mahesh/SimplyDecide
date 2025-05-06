package main.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/decisionwise_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Choice saveChoice(Choice choice) {
        String sql = "INSERT INTO choices (title, decision_id) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, choice.getTitle());
                pstmt.setInt(2, choice.getDecisionId());
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int choiceId = generatedKeys.getInt(1);
                        choice.setId(choiceId);
                        
                        // Save pros and cons with the correct choice_id
                        for (ProCon proCon : choice.getProsAndCons()) {
                            proCon.setChoiceId(choiceId);
                            saveProCon(conn, proCon);
                        }
                        
                        conn.commit();
                        return choice;
                    }
                }
            }
            
            conn.rollback();
            return null;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProCon(Connection conn, ProCon proCon) throws SQLException {
        String sql = "INSERT INTO pros_cons (choice_id, type, content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, proCon.getChoiceId());
            pstmt.setString(2, proCon.getType().toString());
            pstmt.setString(3, proCon.getContent());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    proCon.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void saveProCon(ProCon proCon) {
        try (Connection conn = getConnection()) {
            saveProCon(conn, proCon);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Choice> getAllChoices() {
        List<Choice> choices = new ArrayList<>();
        String sql = "SELECT * FROM choices ORDER BY created_at DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Choice choice = new Choice(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getTimestamp("created_at")
                );
                loadProsAndCons(choice);
                choices.add(choice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return choices;
    }

    private void loadProsAndCons(Choice choice) {
        String sql = "SELECT * FROM pros_cons WHERE choice_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, choice.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProCon proCon = new ProCon(
                        rs.getInt("id"),
                        rs.getInt("choice_id"),
                        ProCon.Type.valueOf(rs.getString("type")),
                        rs.getString("content")
                    );
                    choice.addProCon(proCon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteChoice(int choiceId) {
        String sql = "DELETE FROM choices WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, choiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChoice(Choice choice) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Update choice title
            String sql = "UPDATE choices SET title = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, choice.getTitle());
                pstmt.setInt(2, choice.getId());
                pstmt.executeUpdate();
            }

            // Delete existing pros and cons
            sql = "DELETE FROM pros_cons WHERE choice_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, choice.getId());
                pstmt.executeUpdate();
            }

            // Insert new pros and cons
            for (ProCon proCon : choice.getProsAndCons()) {
                proCon.setChoiceId(choice.getId());
                saveProCon(conn, proCon);
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Decision-related methods
    public Decision createDecision(Decision decision) {
        String sql = "INSERT INTO decisions (title) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, decision.getTitle());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    decision.setId(generatedKeys.getInt(1));
                }
            }
            return decision;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Decision> getAllDecisions() {
        List<Decision> decisions = new ArrayList<>();
        String sql = "SELECT * FROM decisions ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Decision decision = new Decision(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getTimestamp("created_at")
                );
                decisions.add(decision);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decisions;
    }

    public List<Choice> getChoicesForDecision(int decisionId) {
        List<Choice> choices = new ArrayList<>();
        String sql = "SELECT * FROM choices WHERE decision_id = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, decisionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Choice choice = new Choice(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("decision_id")
                    );
                    loadProsAndCons(choice);
                    choices.add(choice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return choices;
    }

    public void deleteDecision(int decisionId) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            // Delete pros/cons for all choices of this decision
            String getChoicesSql = "SELECT id FROM choices WHERE decision_id = ?";
            try (PreparedStatement getChoicesStmt = conn.prepareStatement(getChoicesSql)) {
                getChoicesStmt.setInt(1, decisionId);
                try (ResultSet rs = getChoicesStmt.executeQuery()) {
                    while (rs.next()) {
                        int choiceId = rs.getInt("id");
                        try (PreparedStatement delProsCons = conn.prepareStatement("DELETE FROM pros_cons WHERE choice_id = ?")) {
                            delProsCons.setInt(1, choiceId);
                            delProsCons.executeUpdate();
                        }
                    }
                }
            }
            // Delete choices
            try (PreparedStatement delChoices = conn.prepareStatement("DELETE FROM choices WHERE decision_id = ?")) {
                delChoices.setInt(1, decisionId);
                delChoices.executeUpdate();
            }
            // Delete decision
            try (PreparedStatement delDecision = conn.prepareStatement("DELETE FROM decisions WHERE id = ?")) {
                delDecision.setInt(1, decisionId);
                delDecision.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 