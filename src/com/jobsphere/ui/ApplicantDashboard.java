package com.jobsphere.ui;

import com.jobsphere.model.Applicant;
import com.jobsphere.ui.applicant.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard for Applicants
 */
public class ApplicantDashboard extends JFrame {
    private Applicant applicant;
    private JTabbedPane tabbedPane;

    public ApplicantDashboard(Applicant applicant) {
        this.applicant = applicant;
        initComponents();
    }

    private void initComponents() {
        setTitle("JobSphere - Welcome, " + applicant.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + applicant.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Search Jobs", new JobSearchPanel(applicant));
        tabbedPane.addTab("My Applications", new MyApplicationsPanel(applicant));
        tabbedPane.addTab("Saved Jobs", new SavedJobsPanel(applicant));
        tabbedPane.addTab("Profile", new ApplicantProfilePanel(applicant));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void logout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}