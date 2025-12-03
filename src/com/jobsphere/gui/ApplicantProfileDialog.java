// ApplicantProfileDialog.java
package com.jobsphere.gui;

import com.jobsphere.model.Applicant;
import javax.swing.*;
import java.awt.*;

public class ApplicantProfileDialog extends JDialog {

    public ApplicantProfileDialog(JFrame parent, Applicant applicant) {
        super(parent, "My Profile", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(applicant.getName());
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(new JLabel(applicant.getEmail()));

        formPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField(applicant.getPhone());
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Skills:"));
        JTextField skillsField = new JTextField(String.join(", ", applicant.getSkills()));
        formPanel.add(skillsField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            applicant.setName(nameField.getText());
            applicant.setPhone(phoneField.getText());
            JOptionPane.showMessageDialog(this, "Profile updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
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