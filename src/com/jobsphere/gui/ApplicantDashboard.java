// ApplicantDashboard.java
package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Application;
import com.jobsphere.model.Job;
import com.jobsphere.strategy.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicantDashboard extends JFrame {
    private Applicant applicant;
    private JobManagementFacade jobFacade;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public ApplicantDashboard(Applicant applicant) {
        this.applicant = applicant;
        this.jobFacade = new JobManagementFacade();
        initComponents();
        loadJobs();
    }

    private void initComponents() {
        setTitle("JobSphere - Applicant Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with welcome and profile
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + applicant.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton profileButton = new JButton("My Profile");
        profileButton.addActionListener(e -> showProfile());
        topPanel.add(profileButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Job Search Tab
        tabbedPane.addTab("Search Jobs", createJobSearchPanel());

        // My Applications Tab
        tabbedPane.addTab("My Applications", createApplicationsPanel());

        // Saved Jobs Tab
        tabbedPane.addTab("Saved Jobs", createSavedJobsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createJobSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchTypeCombo = new JComboBox<>(new String[]{"Keyword", "Category", "Location"});
        searchPanel.add(searchTypeCombo);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadJobs();
        });
        searchPanel.add(clearButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Job table
        String[] columns = {"Job ID", "Title", "Company", "Location", "Salary", "Type"};
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

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewJobDetails());
        buttonPanel.add(viewButton);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyForJob());
        buttonPanel.add(applyButton);

        JButton saveButton = new JButton("Save Job");
        saveButton.addActionListener(e -> saveJob());
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columns = {"Job Title", "Company", "Applied Date", "Status"};
        DefaultTableModel appTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable appTable = new JTable(appTableModel);

        // Load applications
        List<Application> applications = jobFacade.getApplicantApplications(applicant.getId());
        for (Application app : applications) {
            Job job = jobFacade.getJobDetails(app.getJobId());
            if (job != null) {
                appTableModel.addRow(new Object[]{
                        job.getTitle(),
                        "Company",
                        new java.util.Date(app.getAppliedDate()),
                        app.getStatus()
                });
            }
        }

        panel.add(new JScrollPane(appTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSavedJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columns = {"Job Title", "Location", "Salary", "Posted Date"};
        DefaultTableModel savedTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable savedTable = new JTable(savedTableModel);

        // Load saved jobs
        List<String> savedJobIds = jobFacade.getSavedJobs(applicant.getId());
        for (String jobId : savedJobIds) {
            Job job = jobFacade.getJobDetails(jobId);
            if (job != null) {
                savedTableModel.addRow(new Object[]{
                        job.getTitle(),
                        job.getLocation(),
                        job.getSalary(),
                        new java.util.Date(job.getPostedDate())
                });
            }
        }

        panel.add(new JScrollPane(savedTable), BorderLayout.CENTER);

        return panel;
    }

    private void loadJobs() {
        tableModel.setRowCount(0);
        List<Job> jobs = jobFacade.getAllActiveJobs();
        for (Job job : jobs) {
            tableModel.addRow(new Object[]{
                    job.getId(),
                    job.getTitle(),
                    "Company",
                    job.getLocation(),
                    job.getSalary(),
                    job.getEmploymentType()
            });
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadJobs();
            return;
        }

        String searchType = (String) searchTypeCombo.getSelectedItem();
        JobSearchContext context = new JobSearchContext();

        // Strategy pattern for different search types
        switch (searchType) {
            case "Keyword":
                context.setStrategy(new KeywordSearchStrategy());
                break;
            case "Category":
                context.setStrategy(new CategorySearchStrategy());
                break;
            case "Location":
                context.setStrategy(new LocationSearchStrategy());
                break;
        }

        List<Job> allJobs = jobFacade.getAllActiveJobs();
        List<Job> results = context.executeSearch(allJobs, keyword);

        tableModel.setRowCount(0);
        for (Job job : results) {
            tableModel.addRow(new Object[]{
                    job.getId(),
                    job.getTitle(),
                    "Company",
                    job.getLocation(),
                    job.getSalary(),
                    job.getEmploymentType()
            });
        }
    }

    private void viewJobDetails() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        Job job = jobFacade.getJobDetails(jobId);

        new JobDetailsDialog(this, job, applicant).setVisible(true);
    }

    private void applyForJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);

        if (jobFacade.applyForJob(applicant.getId(), jobId)) {
            JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You have already applied for this job", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        jobFacade.saveJob(applicant.getId(), jobId);
        JOptionPane.showMessageDialog(this, "Job saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showProfile() {
        new ApplicantProfileDialog(this, applicant).setVisible(true);
    }
}