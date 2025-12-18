package com.jobsphere.ui.company;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.model.state.Application;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class CandidateSearchPanel extends JPanel {
    private Company company;
    private JobSphereServiceFacade serviceFacade;
    private JTextField searchField;
    private JTable candidatesTable;
    private DefaultTableModel tableModel;

    public CandidateSearchPanel(Company company) {
        this.company = company;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadAllCandidates();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Candidate Search");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Search by email:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchCandidates());
        topPanel.add(searchButton);

        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(e -> loadAllCandidates());
        topPanel.add(showAllButton);

        add(topPanel, BorderLayout.NORTH);


        String[] columns = {"Applicant Email", "Job Applied For", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        candidatesTable = new JTable(tableModel);
        candidatesTable.setRowHeight(30);
        candidatesTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(candidatesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewResumeButton = new JButton("View Resume");
        viewResumeButton.addActionListener(e -> viewResume());
        buttonPanel.add(viewResumeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAllCandidates() {
        tableModel.setRowCount(0);

        for (String jobId : company.getPostedJobs()) {
            Job job = serviceFacade.getJob(jobId);
            java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);

            for (Application app : applications) {
                tableModel.addRow(new Object[]{
                        app.getApplicantEmail(),
                        job != null ? job.getTitle() : "Unknown",
                        app.getStatus()
                });
            }
        }
    }

    private void searchCandidates() {
        String searchEmail = searchField.getText().trim().toLowerCase();
        if (searchEmail.isEmpty()) {
            loadAllCandidates();
            return;
        }

        tableModel.setRowCount(0);

        for (String jobId : company.getPostedJobs()) {
            Job job = serviceFacade.getJob(jobId);
            java.util.List<Application> applications = serviceFacade.getApplicationsForJob(jobId);

            for (Application app : applications) {
                if (app.getApplicantEmail().toLowerCase().contains(searchEmail)) {
                    tableModel.addRow(new Object[]{
                            app.getApplicantEmail(),
                            job != null ? job.getTitle() : "Unknown",
                            app.getStatus()
                    });
                }
            }
        }
    }

    private void viewResume() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a candidate",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String applicantEmail = (String) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);

        Application foundApp = null;
        for (String jobId : company.getPostedJobs()) {
            Job job = serviceFacade.getJob(jobId);
            if (job != null && job.getTitle().equals(jobTitle)) {
                for (Application app : serviceFacade.getApplicationsForJob(jobId)) {
                    if (app.getApplicantEmail().equals(applicantEmail)) {
                        foundApp = app;
                        break;
                    }
                }
            }
            if (foundApp != null) break;
        }

        if (foundApp != null) {
            JTextArea textArea = new JTextArea(foundApp.getResume());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Resume - " + applicantEmail, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}