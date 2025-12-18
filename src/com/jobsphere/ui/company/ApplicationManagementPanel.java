package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages applications - Uses State Pattern for status transitions
 */
public class ApplicationManagementPanel extends JPanel {
    private Company company;
    private JobSphereServiceFacade serviceFacade;
    private JComboBox<String> jobCombo;
    private JTable applicationsTable;
    private DefaultTableModel tableModel;
    private Map<String, String> jobIdToTitle;

    public ApplicationManagementPanel(Company company) {
        this.company = company;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        this.jobIdToTitle = new HashMap<>();
        initComponents();
        loadJobsList();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Application Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Select Job:"));
        jobCombo = new JComboBox<>();
        jobCombo.addActionListener(e -> loadApplications());
        topPanel.add(jobCombo);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Applicant Email", "Applied Date", "Status"};
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

        // Button panel - STATE PATTERN: Different status transitions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton reviewButton = new JButton("Move to Reviewing");
        reviewButton.addActionListener(e -> changeStatus("Reviewing"));
        buttonPanel.add(reviewButton);

        JButton interviewButton = new JButton("Move to Interview");
        interviewButton.addActionListener(e -> changeStatus("Interview"));
        buttonPanel.add(interviewButton);

        JButton acceptButton = new JButton("Accept");
        acceptButton.setBackground(new Color(46, 204, 113));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.addActionListener(e -> changeStatus("Accepted"));
        buttonPanel.add(acceptButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.setBackground(new Color(231, 76, 60));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.addActionListener(e -> changeStatus("Rejected"));
        buttonPanel.add(rejectButton);

        JButton viewButton = new JButton("View Resume");
        viewButton.addActionListener(e -> viewResume());
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadJobsList() {
        jobCombo.removeAllItems();
        jobIdToTitle.clear();

        for (String jobId : company.getPostedJobs()) {
            Job job = serviceFacade.getJob(jobId);
            if (job != null) {
                jobCombo.addItem(job.getTitle());
                jobIdToTitle.put(job.getTitle(), jobId);
            }
        }

        if (jobCombo.getItemCount() > 0) {
            loadApplications();
        }
    }

    private void loadApplications() {
        tableModel.setRowCount(0);

        String selectedJobTitle = (String) jobCombo.getSelectedItem();
        if (selectedJobTitle == null) return;

        String jobId = jobIdToTitle.get(selectedJobTitle);
        java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        for (Application app : applications) {
            tableModel.addRow(new Object[]{
                    app.getApplicantEmail(),
                    sdf.format(app.getAppliedDate()),
                    app.getStatus()
            });
        }
    }

    private void changeStatus(String newStatus) {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selectedJobTitle = (String) jobCombo.getSelectedItem();
        String jobId = jobIdToTitle.get(selectedJobTitle);
        java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);

        if (selectedRow < applications.size()) {
            Application app = applications.get(selectedRow);

            // STATE PATTERN: Check if transition is valid
            if (!app.canTransitionTo(newStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Cannot transition from " + app.getStatus() + " to " + newStatus,
                        "Invalid Transition", JOptionPane.WARNING_MESSAGE);
                return;
            }

            serviceFacade.updateApplicationStatus(app, newStatus, selectedJobTitle);
            JOptionPane.showMessageDialog(this, "Application status updated",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadApplications();
        }
    }

    private void viewResume() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selectedJobTitle = (String) jobCombo.getSelectedItem();
        String jobId = jobIdToTitle.get(selectedJobTitle);
        java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);

        if (selectedRow < applications.size()) {
            Application app = applications.get(selectedRow);
            JTextArea textArea = new JTextArea(app.getResume());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Resume - " + app.getApplicantEmail(), JOptionPane.INFORMATION_MESSAGE);
        }
    }
}