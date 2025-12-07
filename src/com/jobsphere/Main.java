package com.jobsphere;

import com.jobsphere.ui.LoginFrame;
import com.jobsphere.database.DatabaseManager;

import javax.swing.*;

/**
 * Main entry point for the JobSphere application
 * Initializes the database and launches the login screen
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database connection using Singleton pattern
        DatabaseManager.getInstance().initialize();

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}