// CompanyDashboard.java
package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Application;
import com.jobsphere.model.Company;
import com.jobsphere.model.Job;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CompanyDashboard extends JFrame {
    private Company company;
    private JobManagementFacade jobFacade;
    private JTable jobTable;
    private DefaultTableModel tableModel;

    public CompanyDashboard(Company company) {
        this.company = company;
        this.jobFacade = new JobManagementFacade();
        initComponents();
        loadPostedJobs();
    }

    private void initComponents() {
        setTitle("JobSphere - Company Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + company.getCompanyName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton postJobButton = new JButton("Post New Job");
        postJobButton.setBackground(new Color(60, 179, 113));
        postJobButton.setForeground(Color.WHITE);
        postJobButton.addActionListener(e -> postNewJob());
        topPanel.add(postJobButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Posted Jobs Tab
        tabbedPane.addTab("My Posted Jobs", createPostedJobsPanel());

        // Applications Tab
        tabbedPane.addTab("Applications", createApplicationsPanel());

        // Candidate Search Tab
        tabbedPane.addTab("Search Candidates", createCandidateSearchPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createPostedJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Job table
        String[] columns = {"Job ID", "Title", "Location", "Status", "Applicants", "Posted Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobTable = new JTable(tableModel);
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobTable.getColumnModel().getColumn(0).setMinWidth(0);
        jobTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton editButton = new JButton("Edit Job");
        editButton.addActionListener(e -> editJob());
        buttonPanel.add(editButton);

        JButton toggleButton = new JButton("Toggle Status");
        toggleButton.addActionListener(e -> toggleJobStatus());
        buttonPanel.add(toggleButton);

        JButton deleteButton = new JButton("Delete Job");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteJob());
        buttonPanel.add(deleteButton);

        JButton viewAppsButton = new JButton("View Applications");
        viewAppsButton.setBackground(new Color(70, 130, 180));
        viewAppsButton.setForeground(Color.WHITE);
        viewAppsButton.addActionListener(e -> viewApplications());
        buttonPanel.add(viewAppsButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columns = {"Job Title", "Applicant", "Email", "Applied Date", "Status"};
        DefaultTableModel appTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable appTable = new JTable(appTableModel);

        // Load all applications for company's jobs
        List<String> jobIds = company.getPostedJobIds();
        for (String jobId : jobIds) {
            Job job = jobFacade.getJobDetails(jobId);
            if (job != null) {
                List<Application> applications = jobFacade.getJobApplications(jobId);
                for (Application app : applications) {
                    Applicant applicant = jobFacade.getApplicantById(app.getApplicantId());
                    if (applicant != null) {
                        appTableModel.addRow(new Object[]{
                                job.getTitle(),
                                applicant.getName(),
                                applicant.getEmail(),
                                new java.util.Date(app.getAppliedDate()),
                                app.getStatus()
                        });
                    }
                }
            }
        }

        panel.add(new JScrollPane(appTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCandidateSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Candidates:"));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        DefaultTableModel candidateTableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "Phone", "Skills"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable candidateTable = new JTable(candidateTableModel);

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            candidateTableModel.setRowCount(0);

            if (!keyword.isEmpty()) {
                List<Applicant> candidates = jobFacade.searchCandidates(keyword);
                for (Applicant candidate : candidates) {
                    candidateTableModel.addRow(new Object[]{
                            candidate.getName(),
                            candidate.getEmail(),
                            candidate.getPhone(),
                            String.join(", ", candidate.getSkills())
                    });
                }
            }
        });

        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        panel.add(new JScrollPane(candidateTable), BorderLayout.CENTER);

        return panel;
    }

    private void loadPostedJobs() {
        tableModel.setRowCount(0);
        List<String> jobIds = company.getPostedJobIds();

        for (String jobId : jobIds) {
            Job job = jobFacade.getJobDetails(jobId);
            if (job != null) {
                tableModel.addRow(new Object[]{
                        job.getId(),
                        job.getTitle(),
                        job.getLocation(),
                        job.isActive() ? "Active" : "Paused",
                        job.getApplicantIds().size(),
                        new java.util.Date(job.getPostedDate())
                });
            }
        }
    }

    private void postNewJob() {
        new PostJobDialog(this, company, jobFacade).setVisible(true);
        loadPostedJobs(); // Refresh
    }

    private void editJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        Job job = jobFacade.getJobDetails(jobId);

        new EditJobDialog(this, job, jobFacade).setVisible(true);
        loadPostedJobs(); // Refresh
    }

    private void toggleJobStatus() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        jobFacade.toggleJobStatus(jobId);
        loadPostedJobs(); // Refresh
        JOptionPane.showMessageDialog(this, "Job status updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this job?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String jobId = (String) tableModel.getValueAt(selectedRow, 0);
            jobFacade.deleteJob(jobId, company.getId());
            loadPostedJobs(); // Refresh
            JOptionPane.showMessageDialog(this, "Job deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewApplications() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        Job job = jobFacade.getJobDetails(jobId);

        new ApplicationsDialog(this, job, jobFacade).setVisible(true);
    }
}