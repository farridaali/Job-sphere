package com.jobsphere.gui;

import com.jobsphere.facade.JobManagementFacade;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Job;
import javax.swing.*;
import java.awt.*;

public class JobDetailsDialog extends JDialog {

    public JobDetailsDialog(JFrame parent, Job job, Applicant applicant, JobManagementFacade facade) {
        super(parent, "Job Details", true);
        setSize(600, 550);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(9, 1, 5, 5));
        detailsPanel.add(new JLabel("Title: " + job.getTitle()));
        detailsPanel.add(new JLabel("Location: " + job.getLocation()));
        detailsPanel.add(new JLabel("Salary: " + job.getSalary()));
        detailsPanel.add(new JLabel("Type: " + job.getEmploymentType()));
        detailsPanel.add(new JLabel("Category: " + job.getCategory()));

        JTextArea descArea = new JTextArea("Description:\n" + job.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(550, 100));
        detailsPanel.add(descScroll);

        JTextArea reqArea = new JTextArea("Requirements:\n" + job.getRequirements());
        reqArea.setEditable(false);
        reqArea.setLineWrap(true);
        reqArea.setWrapStyleWord(true);
        JScrollPane reqScroll = new JScrollPane(reqArea);
        reqScroll.setPreferredSize(new Dimension(550, 100));
        detailsPanel.add(reqScroll);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton applyButton = new JButton("Apply for Job");
        applyButton.setBackground(new Color(60, 179, 113));
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            new ApplyJobDialog(parent, job, applicant, facade).setVisible(true);
            dispose();
        });
        buttonPanel.add(applyButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}