package main.view;

import main.model.Choice;
import main.model.ProCon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class EditChoiceDialog extends JDialog {
    private final Choice choice;
    private boolean confirmed = false;
    private JTextField titleField;
    private DefaultListModel<String> prosModel;
    private DefaultListModel<String> consModel;

    public EditChoiceDialog(JFrame parent, Choice choice) {
        super(parent, "Edit Choice", true);
        this.choice = choice;
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initializeUI();
    }

    private void initializeUI() {
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(choice.getTitle());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);

        // Pros and cons panel
        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        listsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Pros list
        JPanel prosPanel = new JPanel(new BorderLayout());
        prosModel = new DefaultListModel<>();
        JList<String> prosList = new JList<>(prosModel);
        JScrollPane prosScroll = new JScrollPane(prosList);
        JLabel prosLabel = new JLabel("Pros");
        prosLabel.setForeground(new Color(46, 125, 50));
        prosPanel.add(prosLabel, BorderLayout.NORTH);
        prosPanel.add(prosScroll, BorderLayout.CENTER);

        JPanel prosButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addProBtn = new JButton("Add");
        JButton editProBtn = new JButton("Edit");
        JButton deleteProBtn = new JButton("Delete");
        prosButtonPanel.add(addProBtn);
        prosButtonPanel.add(editProBtn);
        prosButtonPanel.add(deleteProBtn);
        prosPanel.add(prosButtonPanel, BorderLayout.SOUTH);

        // Cons list
        JPanel consPanel = new JPanel(new BorderLayout());
        consModel = new DefaultListModel<>();
        JList<String> consList = new JList<>(consModel);
        JScrollPane consScroll = new JScrollPane(consList);
        JLabel consLabel = new JLabel("Cons");
        consLabel.setForeground(new Color(198, 40, 40));
        consPanel.add(consLabel, BorderLayout.NORTH);
        consPanel.add(consScroll, BorderLayout.CENTER);

        JPanel consButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addConBtn = new JButton("Add");
        JButton editConBtn = new JButton("Edit");
        JButton deleteConBtn = new JButton("Delete");
        consButtonPanel.add(addConBtn);
        consButtonPanel.add(editConBtn);
        consButtonPanel.add(deleteConBtn);
        consPanel.add(consButtonPanel, BorderLayout.SOUTH);

        listsPanel.add(prosPanel);
        listsPanel.add(consPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add all panels
        add(titlePanel, BorderLayout.NORTH);
        add(listsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load existing pros and cons
        loadProsAndCons();

        // Event handlers
        addProBtn.addActionListener(e -> addItem(true));
        editProBtn.addActionListener(e -> editItem(prosList, true));
        deleteProBtn.addActionListener(e -> deleteItem(prosList, prosModel));

        addConBtn.addActionListener(e -> addItem(false));
        editConBtn.addActionListener(e -> editItem(consList, false));
        deleteConBtn.addActionListener(e -> deleteItem(consList, consModel));

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadProsAndCons() {
        for (ProCon proCon : choice.getProsAndCons()) {
            if (proCon.getType() == ProCon.Type.PRO) {
                prosModel.addElement(proCon.getContent());
            } else {
                consModel.addElement(proCon.getContent());
            }
        }
    }

    private void addItem(boolean isPro) {
        String item = JOptionPane.showInputDialog(this, 
            "Enter " + (isPro ? "pro" : "con") + ":");
        if (item != null && !item.trim().isEmpty()) {
            DefaultListModel<String> model = isPro ? prosModel : consModel;
            model.addElement(item.trim());
        }
    }

    private void editItem(JList<String> list, boolean isPro) {
        int index = list.getSelectedIndex();
        if (index != -1) {
            String oldItem = list.getSelectedValue();
            String newItem = JOptionPane.showInputDialog(this, 
                "Edit " + (isPro ? "pro" : "con") + ":", oldItem);
            if (newItem != null && !newItem.trim().isEmpty()) {
                DefaultListModel<String> model = isPro ? prosModel : consModel;
                model.setElementAt(newItem.trim(), index);
            }
        }
    }

    private void deleteItem(JList<String> list, DefaultListModel<String> model) {
        int index = list.getSelectedIndex();
        if (index != -1) {
            model.remove(index);
        }
    }

    private void save() {
        choice.setTitle(titleField.getText().trim());
        
        // Clear existing pros and cons
        choice.getProsAndCons().clear();
        
        // Add pros
        for (int i = 0; i < prosModel.size(); i++) {
            choice.addProCon(new ProCon(choice.getId(), ProCon.Type.PRO, prosModel.get(i)));
        }
        
        // Add cons
        for (int i = 0; i < consModel.size(); i++) {
            choice.addProCon(new ProCon(choice.getId(), ProCon.Type.CON, consModel.get(i)));
        }
        
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 