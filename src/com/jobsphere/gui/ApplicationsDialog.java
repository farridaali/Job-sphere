package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Application;
import com.jobsphere.model.Job;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ApplicationsDialog extends JDialog {

    public ApplicationsDialog(JFrame parent, Job job, JobManagementFacade facade) {
        super(parent, "Applications for: " + job.getTitle(), true);
        setSize(900, 550);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Application ID", "Applicant", "Email", "Phone", "Resume", "Status", "Applied Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        List<Application> applications = facade.getJobApplications(job.getId());
        for (Application app : applications) {
            Applicant applicant = facade.getApplicantById(app.getApplicantId());
            if (applicant != null) {
                String resumeStatus = (app.getResumePath() != null && !app.getResumePath().isEmpty())
                        ? "✓ Attached"
                        : "✗ No Resume";
                tableModel.addRow(new Object[]{
                        app.getId(),
                        applicant.getName(),
                        applicant.getEmail(),
                        applicant.getPhone(),
                        resumeStatus,
                        app.getStatus(),
                        new java.util.Date(app.getAppliedDate())
                });
            }
        }

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton viewResumeButton = new JButton("View Resume");
        viewResumeButton.setBackground(new Color(70, 130, 180));
        viewResumeButton.setForeground(Color.WHITE);
        viewResumeButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String appId = (String) tableModel.getValueAt(row, 0);
                Application app = applications.stream()
                        .filter(a -> a.getId().equals(appId))
                        .findFirst()
                        .orElse(null);

                if (app != null && app.getResumePath() != null && !app.getResumePath().isEmpty()) {
                    File resumeFile = new File(app.getResumePath());
                    if (resumeFile.exists()) {
                        try {
                            Desktop.getDesktop().open(resumeFile);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Resume Path: " + app.getResumePath() + "\n\nCould not open file automatically.",
                                    "Resume Location",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Resume file not found at: " + app.getResumePath(),
                                "File Not Found",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No resume attached to this application",
                            "No Resume",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select an application",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(viewResumeButton);

        JButton reviewButton = new JButton("Mark as Reviewed");
        reviewButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String appId = (String) tableModel.getValueAt(row, 0);
                facade.updateApplicationStatus(appId, "REVIEWED");
                tableModel.setValueAt("REVIEWED", row, 5);
            }
        });

        JButton acceptButton = new JButton("Accept");
        acceptButton.setBackground(new Color(60, 179, 113));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String appId = (String) tableModel.getValueAt(row, 0);
                facade.updateApplicationStatus(appId, "ACCEPTED");
                tableModel.setValueAt("ACCEPTED", row, 5);
            }
        });

        JButton rejectButton = new JButton("Reject");
        rejectButton.setBackground(new Color(220, 20, 60));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String appId = (String) tableModel.getValueAt(row, 0);
                facade.updateApplicationStatus(appId, "REJECTED");
                tableModel.setValueAt("REJECTED", row, 5);
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(reviewButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}