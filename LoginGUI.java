import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        loginButton.addActionListener(this);
        add(loginButton);

        getContentPane().setBackground(new Color(0x191414));
        setSize(500, 890);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if ("admin".equals(username) && "123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");

                musicPlayer.closeMediaPlayer();
                musicPlayer.setVisible(true);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        }
    }

}
