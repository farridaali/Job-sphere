package com.jobsphere.gui;

import com.jobsphere.factory.UserFactory;
import com.jobsphere.model.Applicant;
import com.jobsphere.model.Company;
import com.jobsphere.model.User;
import com.jobsphere.service.DatabaseService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private DatabaseService database;

    public LoginFrame() {
        this.database = DatabaseService.getInstance();
        initComponents();
    }

    private void initComponents() {
        setTitle("JobSphere - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("JobSphere", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 130, 180));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        centerPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);

        // User Type
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Applicant", "Company"});
        centerPanel.add(userTypeCombo, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> handleLogin());

        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = database.authenticateUser(email, password);

        if (user != null) {
            this.dispose();
            if (user instanceof Applicant) {
                new ApplicantDashboard((Applicant) user).setVisible(true);
            } else if (user instanceof Company) {
                new CompanyDashboard((Company) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        new RegisterFrame(this).setVisible(true);
        this.setVisible(false);
    }
}