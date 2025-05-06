package main.view;

import main.model.Choice;
import main.model.DBManager;
import main.model.ProCon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class ChoiceCard extends JPanel {
    private final Choice choice;
    private final DBManager dbManager;
    private final JPanel prosPanel;
    private final JPanel consPanel;
    private final JCheckBox selectBox;
    private final Consumer<Boolean> onSelectionChanged;

    public ChoiceCard(Choice choice, DBManager dbManager, Consumer<Boolean> onSelectionChanged) {
        this.choice = choice;
        this.dbManager = dbManager;
        this.onSelectionChanged = onSelectionChanged;
        
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(550, 280));
        setPreferredSize(new Dimension(550, 280));
        setBackground(Color.WHITE);

        // Title panel with selection checkbox
        JPanel titlePanel = new JPanel(new BorderLayout(5, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        selectBox = new JCheckBox();
        selectBox.setBackground(Color.WHITE);
        selectBox.addActionListener(e -> onSelectionChanged.accept(selectBox.isSelected()));
        
        JLabel titleLabel = new JLabel(choice.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton editBtn = new JButton("Edit");
        editBtn.setPreferredSize(new Dimension(70, 28));
        
        titlePanel.add(selectBox, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(editBtn, BorderLayout.EAST);

        // Pros and cons panels
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Pros panel
        JPanel prosContainer = new JPanel(new BorderLayout(0, 5));
        prosContainer.setBackground(Color.WHITE);
        JLabel prosLabel = new JLabel("Pros");
        prosLabel.setForeground(new Color(46, 125, 50));
        prosLabel.setFont(new Font("Arial", Font.BOLD, 14));
        prosPanel = new JPanel();
        prosPanel.setLayout(new BoxLayout(prosPanel, BoxLayout.Y_AXIS));
        prosPanel.setBackground(Color.WHITE);
        
        prosContainer.add(prosLabel, BorderLayout.NORTH);
        prosContainer.add(prosPanel, BorderLayout.CENTER);

        // Cons panel
        JPanel consContainer = new JPanel(new BorderLayout(0, 5));
        consContainer.setBackground(Color.WHITE);
        JLabel consLabel = new JLabel("Cons");
        consLabel.setForeground(new Color(198, 40, 40));
        consLabel.setFont(new Font("Arial", Font.BOLD, 14));
        consPanel = new JPanel();
        consPanel.setLayout(new BoxLayout(consPanel, BoxLayout.Y_AXIS));
        consPanel.setBackground(Color.WHITE);
        
        consContainer.add(consLabel, BorderLayout.NORTH);
        consContainer.add(consPanel, BorderLayout.CENTER);

        contentPanel.add(prosContainer);
        contentPanel.add(consContainer);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Load pros and cons
        loadProsAndCons();

        // Event handlers
        editBtn.addActionListener(e -> showEditDialog());
    }

    private void loadProsAndCons() {
        prosPanel.removeAll();
        consPanel.removeAll();

        for (ProCon proCon : choice.getProsAndCons()) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(new EmptyBorder(2, 0, 2, 0));
            
            String bulletPoint = proCon.getType() == ProCon.Type.PRO ? "✓ " : "✗ ";
            JLabel label = new JLabel(bulletPoint + proCon.getContent());
            label.setFont(new Font("Arial", Font.PLAIN, 13));
            label.setForeground(proCon.getType() == ProCon.Type.PRO ? 
                new Color(46, 125, 50) : new Color(198, 40, 40));
            itemPanel.add(label);

            if (proCon.getType() == ProCon.Type.PRO) {
                prosPanel.add(itemPanel);
            } else {
                consPanel.add(itemPanel);
            }
        }

        prosPanel.revalidate();
        prosPanel.repaint();
        consPanel.revalidate();
        consPanel.repaint();
    }

    private void showEditDialog() {
        EditChoiceDialog dialog = new EditChoiceDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            choice
        );
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            dbManager.updateChoice(choice);
            loadProsAndCons();
        }
    }

    public void setSelected(boolean selected) {
        selectBox.setSelected(selected);
        onSelectionChanged.accept(selected);
    }

    public boolean isSelected() {
        return selectBox.isSelected();
    }
} 