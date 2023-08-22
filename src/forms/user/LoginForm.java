package forms.user;

import application.ConnectToDatabase;
import application.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(email, password);

                if (user != null) {
                    dispose();
                    showData(user);

                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Operation canceled",
                        "Canceled",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
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

            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("           Email: " + user.email);
            System.out.println("           Phone: " + user.phone);
            System.out.println("           Address: " + user.address);

            JOptionPane.showMessageDialog(this,
                    "Email: " + user.email + "\n" +
                            "Phone: " + user.phone + "\n" +
                            "Address: " + user.address, "Successful Authentication of: " + user.name,
                    JOptionPane.PLAIN_MESSAGE);

        } else {
            System.out.println("Authentication canceled");
        }

    }
}
