package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Job;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ApplyJobDialog extends JDialog {
    private JTextField resumePathField;

    public ApplyJobDialog(JFrame parent, Job job, Applicant applicant, JobManagementFacade facade) {
        super(parent, "Apply for Job", true);
        setSize(550, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.add(new JLabel("Applying for: " + job.getTitle()));
        infoPanel.add(new JLabel("Applicant: " + applicant.getName()));
        infoPanel.add(new JLabel("Email: " + applicant.getEmail()));
        infoPanel.add(new JLabel("Phone: " + applicant.getPhone()));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Resume panel
        JPanel resumePanel = new JPanel(new BorderLayout(10, 10));
        resumePanel.setBorder(BorderFactory.createTitledBorder("Resume Upload"));

        JPanel resumeFieldPanel = new JPanel(new BorderLayout(5, 5));
        resumePathField = new JTextField(
                (applicant.getResumePath() != null && !applicant.getResumePath().isEmpty())
                        ? applicant.getResumePath()
                        : "No resume selected"
        );
        resumePathField.setEditable(false);
        resumeFieldPanel.add(new JLabel("Resume: "), BorderLayout.WEST);
        resumeFieldPanel.add(resumePathField, BorderLayout.CENTER);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseResume());
        resumeFieldPanel.add(browseButton, BorderLayout.EAST);

        resumePanel.add(resumeFieldPanel, BorderLayout.NORTH);

        // Info text
        JTextArea infoText = new JTextArea(
                "Please upload your resume to complete your application.\n\n" +
                        "Supported formats: PDF, DOC, DOCX\n\n" +
                        "Your resume will be reviewed by the hiring team."
        );
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setBackground(mainPanel.getBackground());
        resumePanel.add(infoText, BorderLayout.CENTER);

        mainPanel.add(resumePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton submitButton = new JButton("Submit Application");
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setPreferredSize(new Dimension(160, 35));
        submitButton.addActionListener(e -> submitApplication(applicant, job, facade));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void browseResume() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Your Resume");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "PDF and Word Documents (*.pdf, *.doc, *.docx)", "pdf", "doc", "docx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            resumePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void submitApplication(Applicant applicant, Job job, JobManagementFacade facade) {
        String resumePath = resumePathField.getText();

        if (resumePath.equals("No resume selected")) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "No resume selected. Do you want to continue without a resume?",
                    "No Resume",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.NO_OPTION) {
                return;
            }
        }

        // Submit application
        boolean success;
        if (!resumePath.equals("No resume selected")) {
            success = facade.applyForJobWithResume(applicant.getId(), job.getId(), resumePath);
        } else {
            success = facade.applyForJob(applicant.getId(), job.getId());
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Application submitted successfully!\n\nYou will be notified once your application is reviewed.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "You have already applied for this job.",
                    "Already Applied",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}