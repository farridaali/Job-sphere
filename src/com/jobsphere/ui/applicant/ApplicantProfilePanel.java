package com.jobsphere.ui.applicant;

import com.jobsphere.model.Applicant;
import com.jobsphere.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

/**
 * Profile management for applicants
 */
public class ApplicantProfilePanel extends JPanel {
    private Applicant applicant;
    private JTextField nameField, emailField;
    private JTextArea resumeArea, skillsArea;

    public ApplicantProfilePanel(Applicant applicant) {
        this.applicant = applicant;
        initComponents();
        loadProfile();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(30);
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(30);
        emailField.setEditable(false);
        formPanel.add(emailField, gbc);

        // Resume
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Resume/Bio:"), gbc);
        gbc.gridx = 1;
        resumeArea = new JTextArea(5, 30);
        resumeArea.setLineWrap(true);
        resumeArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(resumeArea), gbc);

        // Skills
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Skills (one per line):"), gbc);
        gbc.gridx = 1;
        skillsArea = new JTextArea(5, 30);
        skillsArea.setLineWrap(true);
        formPanel.add(new JScrollPane(skillsArea), gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Profile");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveProfile());
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProfile() {
        nameField.setText(applicant.getName());
        emailField.setText(applicant.getEmail());

        if (applicant.getResume() != null) {
            resumeArea.setText(applicant.getResume());
        }

        if (!applicant.getSkills().isEmpty()) {
            skillsArea.setText(String.join("\n", applicant.getSkills()));
        }
    }

    private void saveProfile() {
        applicant.setName(nameField.getText().trim());
        applicant.setResume(resumeArea.getText().trim());

        // Update skills
        applicant.getSkills().clear();
        String[] skills = skillsArea.getText().split("\n");
        for (String skill : skills) {
            if (!skill.trim().isEmpty()) {
                applicant.addSkill(skill.trim());
            }
        }

        DatabaseManager.getInstance().saveUser(applicant);
        JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}