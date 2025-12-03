// JobDetailsDialog.java
package com.jobsphere.gui;

import com.jobsphere.model.Applicant;
import com.jobsphere.model.Job;
import javax.swing.*;
import java.awt.*;

public class JobDetailsDialog extends JDialog {

    public JobDetailsDialog(JFrame parent, Job job, Applicant applicant) {
        super(parent, "Job Details", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        detailsPanel.add(new JLabel("Title: " + job.getTitle()));
        detailsPanel.add(new JLabel("Location: " + job.getLocation()));
        detailsPanel.add(new JLabel("Salary: " + job.getSalary()));
        detailsPanel.add(new JLabel("Type: " + job.getEmploymentType()));
        detailsPanel.add(new JLabel("Category: " + job.getCategory()));

        JTextArea descArea = new JTextArea("Description:\n" + job.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(descArea));

        JTextArea reqArea = new JTextArea("Requirements:\n" + job.getRequirements());
        reqArea.setEditable(false);
        reqArea.setLineWrap(true);
        reqArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(reqArea));

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
