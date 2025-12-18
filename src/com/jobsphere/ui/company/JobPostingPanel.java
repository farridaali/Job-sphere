package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.builder.JobBuilder;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import java.awt.*;
import java.util.*;


public class JobPostingPanel extends JPanel {
    private Company company;
    private JobSphereServiceFacade serviceFacade;
    private JTextField titleField, locationField, salaryField;
    private JComboBox<String> jobTypeCombo;
    private JTextArea descriptionArea, requirementsArea, responsibilitiesArea;

    public JobPostingPanel(Company company) {
        this.company = company;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Post a New Job");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:*"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(30);
        formPanel.add(titleField, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(30);
        formPanel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Job Type:"), gbc);
        gbc.gridx = 1;
        jobTypeCombo = new JComboBox<>(new String[]{"Full-time", "Part-time", "Contract", "Internship"});
        formPanel.add(jobTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Salary Range:"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(30);
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Requirements (one per line):"), gbc);
        gbc.gridx = 1;
        requirementsArea = new JTextArea(3, 30);
        requirementsArea.setLineWrap(true);
        formPanel.add(new JScrollPane(requirementsArea), gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Responsibilities (one per line):"), gbc);
        gbc.gridx = 1;
        responsibilitiesArea = new JTextArea(3, 30);
        responsibilitiesArea.setLineWrap(true);
        formPanel.add(new JScrollPane(responsibilitiesArea), gbc);

        JScrollPane formScrollPane = new JScrollPane(formPanel);
        add(formScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton postButton = new JButton("Post Job");
        postButton.setBackground(new Color(46, 204, 113));
        postButton.setForeground(Color.WHITE);
        postButton.addActionListener(e -> postJob());
        buttonPanel.add(postButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void postJob() {
        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Job title is required",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Job job =new Job(company.getEmail(), title,descriptionArea.getText().trim());
        JobBuilder builder=new JobBuilder();
        builder.createNewJob(job);
        builder.buildlocation(locationField.getText().trim());
        //builder.builddescription(descriptionArea.getText().trim());
        builder.buildType((String) jobTypeCombo.getSelectedItem());
        builder.buildsalary(salaryField.getText().trim());



        String[] reqLines = requirementsArea.getText().split("\n");
        java.util.List<String> requirements = new ArrayList<>();
        for (String req : reqLines) {
            if (!req.trim().isEmpty()) {
                requirements.add(req.trim());
            }
        }
        builder.buildrequirements(requirements);

        String[] respLines = responsibilitiesArea.getText().split("\n");
        java.util.List<String> responsibilities = new ArrayList<>();
        for (String resp : respLines) {
            if (!resp.trim().isEmpty()) {
                responsibilities.add(resp.trim());
            }
        }
        builder.buildresponsibilities(responsibilities);

        job = builder.build();
        serviceFacade.postJob(job, company);

        JOptionPane.showMessageDialog(this, "Job posted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void clearForm() {
        titleField.setText("");
        locationField.setText("");
        salaryField.setText("");
        descriptionArea.setText("");
        requirementsArea.setText("");
        responsibilitiesArea.setText("");
        jobTypeCombo.setSelectedIndex(0);
    }
}