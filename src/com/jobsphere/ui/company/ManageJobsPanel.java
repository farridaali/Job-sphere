package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing posted jobs (edit status, view applications)
 */
public class ManageJobsPanel extends JPanel {
    private Company company;
    private JobSphereServiceFacade serviceFacade;
    private JTable jobsTable;
    private DefaultTableModel tableModel;

    public ManageJobsPanel(Company company) {
        this.company = company;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadJobs();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Manage My Job Postings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Job Title", "Location", "Type", "Status", "Applications"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobsTable = new JTable(tableModel);
        jobsTable.setRowHeight(30);
        jobsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(jobsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton pauseButton = new JButton("Pause Job");
        pauseButton.addActionListener(e -> changeJobStatus(Job.JobStatus.PAUSED));
        buttonPanel.add(pauseButton);

        JButton activateButton = new JButton("Activate Job");
        activateButton.addActionListener(e -> changeJobStatus(Job.JobStatus.ACTIVE));
        buttonPanel.add(activateButton);

        JButton closeButton = new JButton("Close Job");
        closeButton.addActionListener(e -> changeJobStatus(Job.JobStatus.CLOSED));
        buttonPanel.add(closeButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadJobs());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadJobs() {
        tableModel.setRowCount(0);
        List<String> postedJobIds = company.getPostedJobs();

        for (String jobId : postedJobIds) {
            Job job = serviceFacade.getJob(jobId);
            if (job != null) {
                int applicationCount = serviceFacade.getApplicationsForJob(jobId).size();
                tableModel.addRow(new Object[]{
                        job.getTitle(),
                        job.getLocation(),
                        job.getJobType(),
                        job.getStatus(),
                        applicationCount
                });
            }
        }
    }

    private void changeJobStatus(Job.JobStatus newStatus) {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobTitle = (String) tableModel.getValueAt(selectedRow, 0);
        String jobId = company.getPostedJobs().get(selectedRow);

        serviceFacade.updateJobStatus(jobId, newStatus);
        JOptionPane.showMessageDialog(this, "Job status updated to: " + newStatus,
                "Success", JOptionPane.INFORMATION_MESSAGE);
        loadJobs();
    }
}