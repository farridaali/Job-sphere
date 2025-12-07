package com.jobsphere.ui.applicant;
import com.jobsphere.search.*;
import com.jobsphere.model.*;
import com.jobsphere.service.JobSphereServiceFacade;
import com.jobsphere.search.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Job search panel - Uses Strategy Pattern for different search types
 */
public class JobSearchPanel extends JPanel {
    private Applicant applicant;
    private JobSphereServiceFacade serviceFacade;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JPanel resultsPanel;

    public JobSearchPanel(Applicant applicant) {
        this.applicant = applicant;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadAllJobs();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Search by:"));
        searchTypeCombo = new JComboBox<>(new String[]{"Keyword", "Location", "Job Type"});
        searchPanel.add(searchTypeCombo);

        searchField = new JTextField(30);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(41, 128, 185));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadAllJobs();
        });
        searchPanel.add(clearButton);

        add(searchPanel, BorderLayout.NORTH);

        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        String criteria = searchField.getText().trim();
        if (criteria.isEmpty()) {
            loadAllJobs();
            return;
        }

        // STRATEGY PATTERN: Select search strategy based on user choice
        JobSearchContext context = new JobSearchContext();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        switch (searchType) {
            case "Keyword":
                context.setStrategy(new KeywordSearchStrategy());
                break;
            case "Location":
                context.setStrategy(new LocationSearchStrategy());
                break;
            case "Job Type":
                context.setStrategy(new JobTypeSearchStrategy());
                break;
        }

        List<Job> allJobs = serviceFacade.searchJobs();
        List<Job> results = context.executeSearch(allJobs, criteria);
        displayResults(results);
    }

    private void loadAllJobs() {
        List<Job> jobs = serviceFacade.searchJobs();
        displayResults(jobs);
    }

    private void displayResults(List<Job> jobs) {
        resultsPanel.removeAll();

        if (jobs.isEmpty()) {
            resultsPanel.add(new JLabel("No jobs found"));
        } else {
            for (Job job : jobs) {
                if (job.getStatus() == Job.JobStatus.ACTIVE) {
                    resultsPanel.add(createJobCard(job));
                    resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createJobCard(Job job) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.add(new JLabel("<html><b style='font-size:14px'>" + job.getTitle() + "</b></html>"));
        infoPanel.add(new JLabel("Location: " + (job.getLocation() != null ? job.getLocation() : "N/A")));
        infoPanel.add(new JLabel("Type: " + (job.getJobType() != null ? job.getJobType() : "N/A")));
        infoPanel.add(new JLabel("Salary: " + (job.getSalary() != null ? job.getSalary() : "N/A")));
        card.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewJobDetails(job));
        buttonPanel.add(viewButton);

        JButton saveButton = new JButton(applicant.isJobSaved(job.getId()) ? "Unsave" : "Save");
        saveButton.addActionListener(e -> toggleSaveJob(job, saveButton));
        buttonPanel.add(saveButton);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void viewJobDetails(Job job) {
        new JobDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), job, applicant).setVisible(true);
    }

    private void toggleSaveJob(Job job, JButton button) {
        if (applicant.isJobSaved(job.getId())) {
            serviceFacade.unsaveJobForApplicant(applicant, job.getId());
            button.setText("Save");
        } else {
            serviceFacade.saveJobForApplicant(applicant, job.getId());
            button.setText("Unsave");
        }
    }
}