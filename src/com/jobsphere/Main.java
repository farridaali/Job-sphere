package com.jobsphere;

import com.jobsphere.ui.LoginFrame;
import com.jobsphere.database.DatabaseManager;

import javax.swing.*;

/**
 * dah el main entry lel application
 * we ben-initalize fy el database we bn-launch el login screen
 */
public class Main {
    public static void main(String[] args) {

        DatabaseManager.getInstance().initialize();

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}