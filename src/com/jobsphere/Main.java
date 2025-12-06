package com.jobsphere;

import com.jobsphere.gui.LoginFrame;
import com.jobsphere.service.DatabaseService;
import javax.swing.SwingUtilities;

/**
 * Main entry point for JobSphere application
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database (Singleton pattern ensures single instance)
        DatabaseService.getInstance();

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}