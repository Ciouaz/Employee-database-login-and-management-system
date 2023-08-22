package forms.admin;

import application.ConnectToDatabase;
import application.User;
import forms.user.RegistrationForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminForm extends JFrame {
    private JTable tbDatabase;
    private JButton btnDisplayData;
    private JButton updateButton;
    private JButton btnClear;
    private JPanel JPanel;
    private JPanel adminPanel;
    private JTextField tfEmail;
    private JButton deleteButton;
    private JButton addDataButton;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JTextField tfName;
    private JTextField tfPassword;
    private JLabel labelId;

    AdminForm(JFrame parent) {

        setTitle("Admin panel");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(700, 800));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        displayData();

        btnDisplayData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });


        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tbDatabase.setModel(new DefaultTableModel());
            }

        });


        tbDatabase.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                getSelectedData();
                labelId.setText(getSelectedData()[0]);
                tfName.setText(getSelectedData()[1]);
                tfEmail.setText(getSelectedData()[2]);
                tfPhone.setText(getSelectedData()[3]);
                tfAddress.setText(getSelectedData()[4]);
                tfPassword.setText(getSelectedData()[5]);

            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = " + getSelectedData()[0] + ";";

                addUserToDatabase(sql,
                        tfName.getText(),
                        tfEmail.getText(),
                        tfPhone.getText(),
                        tfAddress.getText(),
                        tfPassword.getText()
                );
                displayData();
            }
        });


        addDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql = "INSERT INTO users (name, email, phone, address, password) " +
                        "VALUES (?, ?, ?, ?, ?)";

                addUserToDatabase(
                        sql,
                tfName.getText(),
                tfEmail.getText(),
                tfPhone.getText(),
                tfAddress.getText(),
                tfPassword.getText()
                );
                displayData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        setVisible(true);
    }

    public String[] getSelectedData(){
        DefaultTableModel tbModel = (DefaultTableModel) tbDatabase.getModel();

        return new String[] {
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 0).toString(),
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 1).toString(),
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 2).toString(),
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 3).toString(),
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 4).toString(),
        tbModel.getValueAt(tbDatabase.getSelectedRow(), 5).toString(),

      };

    }

    public void displayData() {

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        tbDatabase.setModel(tableModel);

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();

            DefaultTableModel model = (DefaultTableModel) tbDatabase.getModel();

            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];

            for (int i = 0; i < cols; i++) {
                colName[i] = rsmd.getColumnName(i + 1);
                model.setColumnIdentifiers(colName);
            }

            String id, name, email, phone, address, password;

            while (resultSet.next()) {
                id = resultSet.getString(1);
                name = resultSet.getString(2);
                email = resultSet.getString(3);
                phone = resultSet.getString(4);
                address = resultSet.getString(5);
                password = resultSet.getString(6);

                String[] row = {id, name, email, phone, address, password};
                model.addRow(row);
            }

            stmt.close();
            conn.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }




    private  User addUserToDatabase(String sql, String name, String email, String phone, String address, String password) {

        User user = null;

        try {
            Connection conn = DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );

            Statement stmt = conn.createStatement();

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

           Connection connEmail = DriverManager.getConnection(
                            ConnectToDatabase.DB_URL,
                            ConnectToDatabase.USERNAME,
                            ConnectToDatabase.PASSWORD
                    );
                    PreparedStatement statementEmail = connEmail.prepareStatement("SELECT email FROM users where email= ?");
                    statementEmail.setString(1, email);
                    ResultSet resultSetEmail = statementEmail.executeQuery();

                    if (resultSetEmail.next() && !email.equals(tfEmail.getText())) {
                        JOptionPane.showMessageDialog(this,
                                "Email " + email + " already used!",
                                "Email already used",
                                JOptionPane.ERROR_MESSAGE
                        );

                    } else if (email.equals("") || name.equals("") || password.equals("")) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter all required fields",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE
                        );

                    } else {

                        int addedRows = preparedStatement.executeUpdate();

                        if (addedRows > 0) {
                            user = new User();
                            user.name = name;
                            user.email = email;
                            user.phone = phone;
                            user.address = address;
                            user.password = password;

                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Failed to register new user!",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }

            stmt.close();
            conn.close();
            connEmail.close();
            statementEmail.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        AdminForm adminForm = new AdminForm(null);
    }

}
