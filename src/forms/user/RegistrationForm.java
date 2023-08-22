package forms.user;

import application.ConnectToDatabase;
import application.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(500, 550));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }


        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(RegistrationForm.this,
                        "Operation canceled",
                        "Canceled",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all required fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, phone, address, password);
        if (user != null) {

            dispose();
            run(user);

        } else {

            try {
                Connection conn = DriverManager.getConnection(
                        ConnectToDatabase.DB_URL,
                        ConnectToDatabase.USERNAME,
                        ConnectToDatabase.PASSWORD
                );

                PreparedStatement statement = conn.prepareStatement("SELECT email FROM users where email= ?");
                statement.setString(1, tfEmail.getText());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this,
                            "Email " + tfEmail.getText() + " already used!",
                            "Email already used",
                            JOptionPane.ERROR_MESSAGE
                    );

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to register new user!",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

                statement.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public User user;

    private  User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user = null;

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            int addedRows = preparedStatement.executeUpdate();

            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public void run(User user) {

        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
            JOptionPane.showMessageDialog(this,
                    "Successful registration of: " + user.name, "Successful Authentication",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Something went wrong or email: " + tfEmail + "already used",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Registration canceled");
        }
    }
}
