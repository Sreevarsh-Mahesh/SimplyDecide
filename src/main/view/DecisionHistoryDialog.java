package main.view;

import main.model.DBManager;
import main.model.Decision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DecisionHistoryDialog extends JDialog {
    private Decision selectedDecision = null;
    private JList<String> decisionList;
    private List<Decision> decisions;

    public DecisionHistoryDialog(JFrame parent, DBManager dbManager) {
        super(parent, "Decision History", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        decisions = dbManager.getAllDecisions();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Decision d : decisions) {
            listModel.addElement(d.getTitle() + " (" + d.getCreatedAt() + ")");
        }
        decisionList = new JList<>(listModel);
        decisionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(decisionList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton openBtn = new JButton("Open");
        JButton deleteBtn = new JButton("Delete");
        JButton newDecisionBtn = new JButton("New Decision");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(openBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(newDecisionBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = decisionList.getSelectedIndex();
                if (idx >= 0) {
                    selectedDecision = decisions.get(idx);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(DecisionHistoryDialog.this, "Please select a decision.");
                }
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = decisionList.getSelectedIndex();
                if (idx >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(DecisionHistoryDialog.this, "Are you sure you want to delete this decision and all its choices?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dbManager.deleteDecision(decisions.get(idx).getId());
                        // Refresh the list
                        decisions = dbManager.getAllDecisions();
                        DefaultListModel<String> newModel = new DefaultListModel<>();
                        for (Decision d : decisions) {
                            newModel.addElement(d.getTitle() + " (" + d.getCreatedAt() + ")");
                        }
                        decisionList.setModel(newModel);
                    }
                } else {
                    JOptionPane.showMessageDialog(DecisionHistoryDialog.this, "Please select a decision to delete.");
                }
            }
        });
        newDecisionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog(DecisionHistoryDialog.this, "Enter a name for your new decision:", "New Decision", JOptionPane.PLAIN_MESSAGE);
                if (title != null && !title.trim().isEmpty()) {
                    Decision newDecision = new Decision(title.trim());
                    newDecision = dbManager.createDecision(newDecision);
                    if (newDecision != null && newDecision.getId() > 0) {
                        selectedDecision = newDecision;
                        dispose();
                    }
                }
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }

    public Decision getSelectedDecision() {
        return selectedDecision;
    }
} 