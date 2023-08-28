package forms.user;

import application.ConnectToDatabase;
import application.MessageDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserForm extends JFrame{
    private JPanel UserFormPanel;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JTextField tfPassword;
    private JButton btnUpdate;
    private JButton btnDelete;
    private String email;

    public UserForm(JFrame parent, String email) {

        this.email = email;

        setTitle("User panel");
        setContentPane(UserFormPanel);
        setMinimumSize(new Dimension(500,550));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        displayData();

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateData();
                displayData();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        setVisible(true);
    }

    public void displayData() {

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, this.email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                String name, email, phone, address, password;

                name = resultSet.getString(2);
                email = resultSet.getString(3);
                phone = resultSet.getString(4);
                address = resultSet.getString(5);
                password = resultSet.getString(6);

                tfName.setText(name);
                tfEmail.setText(email);
                tfPhone.setText(phone);
                tfAddress.setText(address);
                tfPassword.setText(password);
            }

            stmt.close();
            conn.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateData() {

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );

            Statement stmt = conn.createStatement();

            String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE email = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tfName.getText());
            preparedStatement.setString(2, tfEmail.getText());
            preparedStatement.setString(3, tfPhone.getText());
            preparedStatement.setString(4, tfAddress.getText());
            preparedStatement.setString(5, tfPassword.getText());
            preparedStatement.setString(6, this.email);

            Connection connEmail = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );

            PreparedStatement statementEmail = connEmail.prepareStatement("SELECT email FROM users WHERE email= ?");
            statementEmail.setString(1, tfEmail.getText());
            ResultSet resultSetEmail = statementEmail.executeQuery();

            if (resultSetEmail.next()) {

                if (tfEmail.getText().equals(email)) {

                   preparedStatement.executeUpdate();
                   MessageDialogs.dataChangedSuccessfully();

                } else {
                    MessageDialogs.emailAlreadyUsed(tfEmail.getText());
                }

            } else if (tfEmail.getText().equals("") || tfName.getText().equals("") || tfPassword.getText().equals("")) {
                MessageDialogs.enterAllFields();

            } else {
                preparedStatement.executeUpdate();
                this.email = tfEmail.getText();
                MessageDialogs.dataChangedSuccessfully();
            }

            stmt.close();
            conn.close();
            connEmail.close();
            statementEmail.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount() {
        try {

            Connection conn = ConnectToDatabase.connect();
            Statement stmt = conn.createStatement();

            String sql = "DELETE FROM  users WHERE email = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);

            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete your account?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {

            preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Account deleted successfully.",
                        "Delete",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();

            } else {
                MessageDialogs.operationCanceled();
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
