package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Job;
import javax.swing.*;
import java.awt.*;

public class EditJobDialog extends JDialog {

    public EditJobDialog(JFrame parent, Job job, JobManagementFacade facade) {
        super(parent, "Edit Job", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1;
        JTextField titleField = new JTextField(job.getTitle(), 20);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        JTextField salaryField = new JTextField(job.getSalary(), 20);
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(job.getDescription(), 5, 20);
        descArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Requirements:"), gbc);
        gbc.gridx = 1;
        JTextArea reqArea = new JTextArea(job.getRequirements(), 5, 20);
        reqArea.setLineWrap(true);
        formPanel.add(new JScrollPane(reqArea), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            job.setTitle(titleField.getText());
            job.setSalary(salaryField.getText());
            job.setDescription(descArea.getText());
            job.setRequirements(reqArea.getText());
            facade.updateJob(job);
            JOptionPane.showMessageDialog(this, "Job updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
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