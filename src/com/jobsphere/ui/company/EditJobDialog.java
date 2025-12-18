package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Dialog for editing existing job postings
 */
public class EditJobDialog extends JDialog {
    private Job job;
    private Company company;
    private JobSphereServiceFacade serviceFacade;

    private JTextField titleField, locationField, salaryField;
    private JComboBox<String> jobTypeCombo;
    private JTextArea descriptionArea, requirementsArea, responsibilitiesArea;

    private boolean saved = false;

    public EditJobDialog(Frame parent, Job job, Company company) {
        super(parent, "Edit Job Posting", true);
        this.job = job;
        this.company = company;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadJobData();
    }

    private void initComponents() {
        setSize(600, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        JLabel titleLabel = new JLabel("Edit Job Posting");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Job Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:*"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(30);
        formPanel.add(titleField, gbc);

        // Location
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(30);
        formPanel.add(locationField, gbc);

        // Job Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Job Type:"), gbc);
        gbc.gridx = 1;
        jobTypeCombo = new JComboBox<>(new String[]{"Full-time", "Part-time", "Contract", "Internship"});
        formPanel.add(jobTypeCombo, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Salary Range:"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(30);
        formPanel.add(salaryField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Requirements
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Requirements:"), gbc);
        gbc.gridx = 1;
        requirementsArea = new JTextArea(3, 30);
        requirementsArea.setLineWrap(true);
        formPanel.add(new JScrollPane(requirementsArea), gbc);

        // Responsibilities
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Responsibilities:"), gbc);
        gbc.gridx = 1;
        responsibilitiesArea = new JTextArea(3, 30);
        responsibilitiesArea.setLineWrap(true);
        formPanel.add(new JScrollPane(responsibilitiesArea), gbc);

        JScrollPane formScrollPane = new JScrollPane(formPanel);
        add(formScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveJob());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadJobData() {
        titleField.setText(job.getTitle());
        locationField.setText(job.getLocation() != null ? job.getLocation() : "");
        salaryField.setText(job.getSalary() != null ? job.getSalary() : "");
        descriptionArea.setText(job.getDescription() != null ? job.getDescription() : "");

        if (job.getJobType() != null) {
            jobTypeCombo.setSelectedItem(job.getJobType());
        }

        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            requirementsArea.setText(String.join("\n", job.getRequirements()));
        }

        if (job.getResponsibilities() != null && !job.getResponsibilities().isEmpty()) {
            responsibilitiesArea.setText(String.join("\n", job.getResponsibilities()));
        }
    }

    private void saveJob() {
        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Job title is required",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update job fields directly
        job.setTitle(title);
        job.setLocation(locationField.getText().trim());
        job.setJobType((String) jobTypeCombo.getSelectedItem());
        job.setSalary(salaryField.getText().trim());
        job.setDescription(descriptionArea.getText().trim());

        // Update requirements
        java.util.List<String> requirements = new ArrayList<>();
        String[] reqLines = requirementsArea.getText().split("\n");
        for (String req : reqLines) {
            if (!req.trim().isEmpty()) {
                requirements.add(req.trim());
            }
        }
        job.setRequirements(requirements);

        // Update responsibilities
        java.util.List<String> responsibilities = new ArrayList<>();
        String[] respLines = responsibilitiesArea.getText().split("\n");
        for (String resp : respLines) {
            if (!resp.trim().isEmpty()) {
                responsibilities.add(resp.trim());
            }
        }
        job.setResponsibilities(responsibilities);

        // Save to database
        serviceFacade.updateJob(job);

        saved = true;
        JOptionPane.showMessageDialog(this, "Job updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean wasSaved() {
        return saved;
    }
}