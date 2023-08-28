package forms.user;

import application.ConnectToDatabase;
import application.MessageDialogs;
import application.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;

public class LoginForm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500, 550));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              login();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               cancel();
            }
        });

        KeyStroke keystrokeCancel = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        loginPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystrokeCancel, "ESCAPE");
        loginPanel.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        KeyStroke keystrokeLogin = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        loginPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystrokeLogin, "ENTER");
        loginPanel.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    public User user;

    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );

            Statement stmt = conn.createStatement();
            String sql = ("SELECT * FROM users WHERE email=? AND password=?");
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public void showData(User user) {

                if (user != null) {

            JOptionPane.showMessageDialog(this,
                    "Successful authentication of: " + user.name + ".", "Success",
                    JOptionPane.PLAIN_MESSAGE);

            new UserForm(null, user.email);

        } else {
            MessageDialogs.operationCanceled();
        }
    }

    private void cancel() {
        MessageDialogs.operationCanceled();
        dispose();
    }

    private void login() {
        String email = tfEmail.getText();
        String password = String.valueOf(pfPassword.getPassword());

        user = getAuthenticatedUser(email, password);

        if (user != null) {
            dispose();
            showData(user);

        } else {
            MessageDialogs.emailOrPasswordInvalid();
        }
    }
}
