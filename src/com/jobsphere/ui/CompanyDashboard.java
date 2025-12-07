package com.jobsphere.ui;

import com.jobsphere.model.Company;
import com.jobsphere.ui.company.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard for Companies
 */
public class CompanyDashboard extends JFrame {
    private Company company;
    private JTabbedPane tabbedPane;

    public CompanyDashboard(Company company) {
        this.company = company;
        initComponents();
    }

    private void initComponents() {
        setTitle("JobSphere - " + company.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(231, 76, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel(company.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Post Job", new JobPostingPanel(company));
        tabbedPane.addTab("Manage Jobs", new ManageJobsPanel(company));
        tabbedPane.addTab("Applications", new ApplicationManagementPanel(company));
        tabbedPane.addTab("Search Candidates", new CandidateSearchPanel(company));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void logout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}