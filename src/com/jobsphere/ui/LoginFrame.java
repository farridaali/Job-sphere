package com.jobsphere.ui;

import com.jobsphere.model.*;
import com.jobsphere.service.JobSphereServiceFacade;


import javax.swing.*;
import java.awt.*;
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JobSphereServiceFacade serviceFacade;

    public LoginFrame() {
        serviceFacade = JobSphereServiceFacade.getInstance();
        initComponents();
    }

    private void initComponents() {
        setTitle("JobSphere - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("JobSphere");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        JButton registerButton = new JButton("Register New Account");
        registerButton.addActionListener(e -> openRegistration());
        mainPanel.add(registerButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = serviceFacade.loginUser(email, password);

        if (user != null) {
            this.dispose();
            if (user.getUserType() == User.UserType.APPLICANT) {
                new ApplicantDashboard((Applicant) user).setVisible(true);
            } else {
                new CompanyDashboard((Company) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        new RegistrationFrame().setVisible(true);
    }
}