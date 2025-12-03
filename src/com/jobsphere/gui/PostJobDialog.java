// PostJobDialog.java
package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Company;
import com.jobsphere.model.Job;
import javax.swing.*;
import java.awt.*;

public class PostJobDialog extends JDialog {

    public PostJobDialog(JFrame parent, Company company, JobManagementFacade facade) {
        super(parent, "Post New Job", true);
        setSize(600, 550);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1;
        JTextField titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Location
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        JTextField locationField = new JTextField(20);
        formPanel.add(locationField, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        JTextField salaryField = new JTextField(20);
        formPanel.add(salaryField, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Full-time", "Part-time", "Contract", "Internship"});
        formPanel.add(typeCombo, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        JTextField categoryField = new JTextField(20);
        formPanel.add(categoryField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descArea), gbc);

        // Requirements
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Requirements:"), gbc);
        gbc.gridx = 1;
        JTextArea reqArea = new JTextArea(5, 20);
        reqArea.setLineWrap(true);
        formPanel.add(new JScrollPane(reqArea), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton postButton = new JButton("Post Job");
        postButton.addActionListener(e -> {
            Job job = new Job.Builder(company.getId(), titleField.getText())
                    .location(locationField.getText())
                    .salary(salaryField.getText())
                    .employmentType((String) typeCombo.getSelectedItem())
                    .category(categoryField.getText())
                    .description(descArea.getText())
                    .requirements(reqArea.getText())
                    .build();

            facade.postJob(job);
            JOptionPane.showMessageDialog(this, "Job posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(postButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}