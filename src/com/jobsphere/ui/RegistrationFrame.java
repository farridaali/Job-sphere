package com.jobsphere.ui;

import com.jobsphere.model.User;
import com.jobsphere.service.JobSphereServiceFacade;

import javax.swing.*;
import java.awt.*;

/**
 * Registration screen - Uses Factory Pattern via ServiceFacade
 */
public class RegistrationFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JobSphereServiceFacade serviceFacade;

    public RegistrationFrame() {
        serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
    }

    private void initComponents() {
        setTitle("JobSphere - Register");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // User Type
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("I am a:"), gbc);
        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Job Seeker", "Company"});
        mainPanel.add(userTypeCombo, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        mainPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Register button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> handleRegistration());
        mainPanel.add(registerButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User.UserType type = userTypeCombo.getSelectedIndex() == 0 ?
                User.UserType.APPLICANT : User.UserType.COMPANY;

        User user = serviceFacade.registerUser(type, email, password, name);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email already exists",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}