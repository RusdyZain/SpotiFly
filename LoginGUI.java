import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginGUI extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private MusicPlayer musicPlayer;
    private JButton registerButton;
    private JTextField nameField;

    public LoginGUI(MusicPlayer musicPlayer) {
        super("Login");
        this.musicPlayer = musicPlayer;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(140, 50, 256, 75);
        ImageIcon originalIcon = new ImageIcon("pngs/logo.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(206, 60, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        logoLabel.setIcon(resizedIcon);
        add(logoLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(140, 125, 80, 25);
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 150, 200, 25);
        usernameField.setForeground(Color.WHITE);
        usernameField.setBackground(new Color(0x191414));
        usernameField.setBorder(new LineBorder(Color.WHITE));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(140, 175, 80, 25);
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 200, 200, 25);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(0x191414));
        passwordField.setBorder(new LineBorder(Color.WHITE));
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(140, 240, 200, 25);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        add(loginButton);

        JLabel infoRegist = new JLabel("Don't have an account yet? Register");
        infoRegist.setBounds(140, 270, 250, 25);
        infoRegist.setForeground(Color.WHITE);
        add(infoRegist);

        registerButton = new JButton("Register");
        registerButton.setBounds(140, 300, 200, 25);
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        add(registerButton);

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        getContentPane().setBackground(new Color(0x191414));
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void openRegistrationPage() {
        JFrame registrationFrame = new JFrame("Register");
        registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registrationFrame.setLayout(null);

        JLabel logoLabel = new JLabel();
        logoLabel.setBounds(140, 50, 256, 75);
        ImageIcon originalIcon = new ImageIcon("pngs/logo.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(206, 60, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        logoLabel.setIcon(resizedIcon);
        registrationFrame.add(logoLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(140, 125, 80, 25);
        nameLabel.setForeground(Color.WHITE);
        registrationFrame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 150, 200, 25);
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(Color.WHITE);
        registrationFrame.add(nameField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(140, 175, 80, 25);
        usernameLabel.setForeground(Color.WHITE);
        registrationFrame.add(usernameLabel);

        JTextField newUsernameField = new JTextField();
        newUsernameField.setBounds(140, 200, 200, 25);
        newUsernameField.setBackground(Color.BLACK);
        newUsernameField.setForeground(Color.WHITE);
        registrationFrame.add(newUsernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(140, 225, 80, 25);
        passwordLabel.setForeground(Color.WHITE);
        registrationFrame.add(passwordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(140, 250, 200, 25);
        newPasswordField.setBackground(Color.BLACK);
        newPasswordField.setForeground(Color.WHITE);
        registrationFrame.add(newPasswordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(140, 290, 200, 25);
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registrationFrame.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameField.getText().trim();
                char[] newPasswordChars = newPasswordField.getPassword();
                String newPassword = new String(newPasswordChars).trim();
                String newName = nameField.getText().trim();

                if (newName.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(registrationFrame, "All fields must be filled.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (registerUser(newName, newUsername, newPassword)) {
                    JOptionPane.showMessageDialog(registrationFrame, "Registration successful!");
                    registrationFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(registrationFrame, "Error registering user. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registrationFrame.getContentPane().setBackground(new Color(0x191414));
        registrationFrame.setSize(500, 400);
        registrationFrame.setLocationRelativeTo(null);
        registrationFrame.setResizable(false);
        registrationFrame.setVisible(true);
    }

    private boolean registerUser(String name, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/sportifly";
            String user = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                String sql = "INSERT INTO user (nama, username, password) VALUES (?, ?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, name);
                    statement.setString(2, username);
                    statement.setString(3, password);

                    int rowsInserted = statement.executeUpdate();

                    return rowsInserted > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars).trim();

            boolean isValidLogin = checkLogin(username, password);

            if (isValidLogin) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                musicPlayer.login(username, password);
                musicPlayer.closeMediaPlayer();
                musicPlayer.setVisible(true);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }

            Arrays.fill(passwordChars, ' ');
        } else if (e.getSource() == registerButton) {
            openRegistrationPage();
        }
    }

    private boolean checkLogin(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/sportifly";
            String user = "root";
            String dbPassword = "";
            try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, username);
                    statement.setString(2, password);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next();
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
