package com.jobsphere.gui;

import com.jobsphere.model.Applicant;
import com.jobsphere.service.DatabaseService;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ApplicantProfileDialog extends JDialog {

    public ApplicantProfileDialog(JFrame parent, Applicant applicant) {
        super(parent, "My Profile", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(applicant.getName(), 20);
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JLabel(applicant.getEmail()), gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(applicant.getPhone(), 20);
        formPanel.add(phoneField, gbc);

        // Skills
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Skills:"), gbc);
        gbc.gridx = 1;
        JTextField skillsField = new JTextField(String.join(", ", applicant.getSkills()), 20);
        formPanel.add(skillsField, gbc);

        // Resume
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Resume:"), gbc);
        gbc.gridx = 1;
        JPanel resumePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField resumePathField = new JTextField(applicant.getResumePath() != null ? applicant.getResumePath() : "No resume uploaded", 15);
        resumePathField.setEditable(false);
        resumePanel.add(resumePathField);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Resume");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "PDF and Word Documents", "pdf", "doc", "docx"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                resumePathField.setText(file.getAbsolutePath());
            }
        });
        resumePanel.add(browseButton);
        formPanel.add(resumePanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 179, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            applicant.setName(nameField.getText());
            applicant.setPhone(phoneField.getText());

            // Update skills
            String skillsText = skillsField.getText();
            if (!skillsText.isEmpty()) {
                applicant.getSkills().clear();
                for (String skill : skillsText.split(",")) {
                    applicant.getSkills().add(skill.trim());
                }
            }

            // Update resume path
            String resumePath = resumePathField.getText();
            if (!resumePath.equals("No resume uploaded")) {
                applicant.setResumePath(resumePath);
            }

            DatabaseService.getInstance().updateUser(applicant);
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}