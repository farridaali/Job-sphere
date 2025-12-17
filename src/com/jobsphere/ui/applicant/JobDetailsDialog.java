package com.jobsphere.ui.applicant;

import com.jobsphere.model.*;
import com.jobsphere.service.JobSphereServiceFacade;
import com.jobsphere.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
public class JobDetailsDialog extends JDialog {
    private Job job;
    private Applicant applicant;
    private JobSphereServiceFacade serviceFacade;

    public JobDetailsDialog(Frame parent, Job job, Applicant applicant) {
        super(parent, "Job Details", true);
        this.job = job;
        this.applicant = applicant;
        this.serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addDetailRow(detailsPanel, "Title:", job.getTitle(), true);
        addDetailRow(detailsPanel, "Location:", job.getLocation(), false);
        addDetailRow(detailsPanel, "Job Type:", job.getJobType(), false);
        addDetailRow(detailsPanel, "Salary:", job.getSalary(), false);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        addDetailRow(detailsPanel, "Description:", job.getDescription(), false);

        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            detailsPanel.add(new JLabel("<html><b>Requirements:</b></html>"));
            for (String req : job.getRequirements()) {
                detailsPanel.add(new JLabel("• " + req));
            }
        }

        if (job.getResponsibilities() != null && !job.getResponsibilities().isEmpty()) {
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            detailsPanel.add(new JLabel("<html><b>Responsibilities:</b></html>"));
            for (String resp : job.getResponsibilities()) {
                detailsPanel.add(new JLabel("• " + resp));
            }
        }

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton("Apply Now");
        applyButton.setBackground(new Color(46, 204, 113));
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> applyForJob());
        buttonPanel.add(applyButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addDetailRow(JPanel panel, String label, String value, boolean bold) {
        if (value != null && !value.isEmpty()) {
            String labelText = bold ? "<html><b style='font-size:16px'>" + label + "</b></html>" :
                    "<html><b>" + label + "</b></html>";
            panel.add(new JLabel(labelText));
            panel.add(new JLabel(value));
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private void applyForJob() {
        // Check if already applied
        for (Application app : serviceFacade.getApplicationsForApplicant(applicant.getEmail())) {
            if (app.getJobId().equals(job.getId())) {
                JOptionPane.showMessageDialog(this, "You have already applied for this job",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        if (applicant.getResume() == null || applicant.getResume().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please update your profile with a resume first",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Application application = new Application(job.getId(), applicant.getEmail(), applicant.getResume());

        // Get company email
        //User companyUser = DatabaseManager.getInstance().getUser(job.getCompanyEmail());
        serviceFacade.submitApplication(application, job.getTitle(), job.getCompanyEmail());

        JOptionPane.showMessageDialog(this, "Application submitted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}