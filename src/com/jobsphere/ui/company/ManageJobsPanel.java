package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;


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


        JLabel titleLabel = new JLabel("Manage My Job Postings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);


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


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton editButton = new JButton("Edit Job");
        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(e -> editJob());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Job");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteJob());
        buttonPanel.add(deleteButton);

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

    private void editJob() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to edit",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = company.getPostedJobs().get(selectedRow);
        Job job = serviceFacade.getJob(jobId);

        if (job != null) {

            java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);
            boolean hasActiveApplications = false;

            for (Application app : applications) {
                if (!app.getStatus().equals("Pending")) {
                    hasActiveApplications = true;
                    break;
                }
            }

            if (hasActiveApplications) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Warning: This job has applications that are being reviewed.\n" +
                                "Editing job details may confuse applicants or evaluators.\n\n" +
                                "Do you still want to edit this job?",
                        "Active Applications Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            EditJobDialog dialog = new EditJobDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    job,
                    company
            );
            dialog.setVisible(true);

            if (dialog.wasSaved()) {
                loadJobs();
            }
        }
    }

    private void deleteJob() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to delete",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = company.getPostedJobs().get(selectedRow);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 0);


        java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);

        if (!applications.isEmpty()) {

            boolean hasActiveApplications = false;
            int pendingCount = 0;
            int totalCount = applications.size();

            for (Application app : applications) {
                if (app.getStatus().equals("Pending")) {
                    pendingCount++;
                } else {
                    hasActiveApplications = true;
                }
            }

            if (hasActiveApplications) {
                JOptionPane.showMessageDialog(this,
                        "Cannot delete this job!\n\n" +
                                "This job has " + totalCount + " application(s) that are being reviewed.\n" +
                                "You can only delete jobs with no applications or only pending applications.\n\n" +
                                "Please close the job instead, or wait until all applications are completed.",
                        "Cannot Delete Job",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (pendingCount > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Warning: This job has " + pendingCount + " pending application(s).\n\n" +
                                "Deleting this job will remove all pending applications.\n" +
                                "Applicants will not be notified.\n\n" +
                                "Are you sure you want to delete this job?",
                        "Confirm Deletion with Pending Applications",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }


        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the job: " + jobTitle + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            serviceFacade.deleteJob(jobId, company);
            JOptionPane.showMessageDialog(this, "Job deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadJobs();
        }
    }
}