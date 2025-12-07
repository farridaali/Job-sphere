package com.jobsphere.ui.applicant;

import com.jobsphere.model.*;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
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
        String[] columns = {"Job Title", "Company", "Applied Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(tableModel);
        applicationsTable.setRowHeight(30);
        applicationsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
                        app.getStatus()
                });
            }
        }
    }
}