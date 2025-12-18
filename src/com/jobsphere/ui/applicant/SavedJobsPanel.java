package com.jobsphere.ui.applicant;

import com.jobsphere.model.*;
import com.jobsphere.model.builder.Job;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SavedJobsPanel extends JPanel {
    private Applicant applicant;
    private JobSphereServiceFacade serviceFacade;
    private JPanel jobsPanel;

    public SavedJobsPanel(Applicant applicant) {
        this.applicant = applicant;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
        loadSavedJobs();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Saved Jobs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);


        jobsPanel = new JPanel();
        jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(jobsPanel);
        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadSavedJobs());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSavedJobs() {
        jobsPanel.removeAll();
        List<String> savedJobIds = applicant.getSavedJobs();

        if (savedJobIds.isEmpty()) {
            jobsPanel.add(new JLabel("No saved jobs yet"));
        } else {
            for (String jobId : savedJobIds) {
                Job job = serviceFacade.getJob(jobId);
                if (job != null) {
                    jobsPanel.add(createJobCard(job));
                    jobsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        jobsPanel.revalidate();
        jobsPanel.repaint();
    }

    private JPanel createJobCard(Job job) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.add(new JLabel("<html><b style='font-size:14px'>" + job.getTitle() + "</b></html>"));
        infoPanel.add(new JLabel("Location: " + (job.getLocation() != null ? job.getLocation() : "N/A")));
        infoPanel.add(new JLabel("Type: " + (job.getJobType() != null ? job.getJobType() : "N/A")));
        card.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> viewJobDetails(job));
        buttonPanel.add(viewButton);

        JButton unsaveButton = new JButton("Unsave");
        unsaveButton.addActionListener(e -> {
            serviceFacade.unsaveJobForApplicant(applicant, job.getId());
            loadSavedJobs();
        });
        buttonPanel.add(unsaveButton);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void viewJobDetails(Job job) {
        new JobDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), job, applicant).setVisible(true);
    }
}