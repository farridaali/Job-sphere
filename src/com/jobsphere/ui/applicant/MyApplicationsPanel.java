package com.jobsphere.ui.applicant;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Shows all applications submitted by the applicant
 */
public class MyApplicationsPanel extends JPanel {
    private Applicant applicant;
    private JobSphereServiceFacade serviceFacade;
    private JTable applicationsTable;
    private DefaultTableModel tableModel;

    public MyApplicationsPanel(Applicant applicant) {
        this.applicant = applicant;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadApplications();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Job Title", "Company", "Applied Date", "Status", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(tableModel);
        applicationsTable.setRowHeight(30);
        applicationsTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        applicationsTable.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(e -> viewApplicationDetails());
        buttonPanel.add(viewDetailsButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadApplications());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        List<Application> applications = serviceFacade.getApplicationsForApplicant(applicant.getEmail());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        for (Application app : applications) {
            Job job = serviceFacade.getJob(app.getJobId());
            if (job != null) {
                tableModel.addRow(new Object[]{
                        job.getTitle(),
                        job.getCompanyEmail(),
                        sdf.format(app.getAppliedDate()),
                        app.getStatus(),
                        app.getStateDescription()  // Show state-specific description
                });
            }
        }
    }

    private void viewApplicationDetails() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobTitle = (String) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 3);
        String description = (String) tableModel.getValueAt(selectedRow, 4);
        Date appliedDate = null;

        List<Application> applications = serviceFacade.getApplicationsForApplicant(applicant.getEmail());
        Application selectedApp = applications.get(selectedRow);

        String message = String.format(
                "Job: %s\n\n" +
                        "Status: %s\n\n" +
                        "Description: %s\n\n" +
                        "Applied: %s\n\n" +
                        "Can be edited: %s\n" +
                        "Final state: %s",
                jobTitle,
                status,
                description,
                new SimpleDateFormat("MMM dd, yyyy").format(selectedApp.getAppliedDate()),
                selectedApp.canBeEdited() ? "Yes" : "No",
                selectedApp.isFinalState() ? "Yes" : "No"
        );

        JOptionPane.showMessageDialog(this, message,
                "Application Details", JOptionPane.INFORMATION_MESSAGE);
    }
}