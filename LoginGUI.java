import javax.swing.*;
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

    public LoginGUI(MusicPlayer musicPlayer) {
        super("Login");
        this.musicPlayer = musicPlayer;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 200, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(190, 200, 200, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 240, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(190, 240, 200, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(200, 280, 100, 25);
        add(loginButton);

        loginButton.addActionListener(this);

        getContentPane().setBackground(new Color(0x191414));
        setSize(500, 890);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
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
