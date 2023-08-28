package forms.admin;

import application.ConnectToDatabase;
import application.MessageDialogs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminForm extends JFrame {
    private JTable tbDatabase;
    private JButton btnDisplayData;
    private JButton updateButton;
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

        // Change to refresh:
        btnDisplayData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });

        tbDatabase.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                getSelectedData();
                labelId.setText("Updating record with id: " + getSelectedData()[0]);
                tfName.setText(getSelectedData()[1]);
                tfEmail.setText(getSelectedData()[2]);
                tfPhone.setText(getSelectedData()[3]);
                tfAddress.setText(getSelectedData()[4]);
                tfPassword.setText(getSelectedData()[5]);

                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
                addDataButton.setEnabled(false);
            }
        });

        // added so user can press ESC button on keyboard to "unclick" selected data
        KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        adminPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystroke, "ESCAPE");
        adminPanel.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateUserInDatabase(
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

                addUserToDatabase(
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
                deleteUserFromDatabase(getSelectedData()[2]);
                displayData();
            }
        });
        setVisible(true);
    }

    public String[] getSelectedData() {
        DefaultTableModel tbModel = (DefaultTableModel) tbDatabase.getModel();

        return new String[]{
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 0).toString(),
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 1).toString(),
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 2).toString(),
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 3).toString(),
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 4).toString(),
                tbModel.getValueAt(tbDatabase.getSelectedRow(), 5).toString(),
        };
    }

    public void displayData() {

        // "unclicking" added button instead of update and delete buttons
        labelId.setText("Adding new record to database.");
        addDataButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // clearing fields for adding new records
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        tfPassword.setText("");

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

    private void addUserToDatabase(String name, String email, String phone, String address, String password) {

        try {
            Connection conn = ConnectToDatabase.connect();
            Statement stmt = conn.createStatement();

            String sql = "INSERT INTO users (name, email, phone, address, password) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            Connection connEmail = ConnectToDatabase.connect();
            PreparedStatement statementEmail = connEmail.prepareStatement(
                    "SELECT email FROM users where email= ?");
            statementEmail.setString(1, email);
            ResultSet resultSetEmail = statementEmail.executeQuery();

            if (resultSetEmail.next()) {
                MessageDialogs.emailAlreadyUsed(email);

            } else if (email.equals("") || name.equals("") || password.equals("")) {
               MessageDialogs.enterAllFields();

            } else {
                preparedStatement.executeUpdate();
            }

            stmt.close();
            conn.close();
            connEmail.close();
            statementEmail.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserInDatabase(String name, String email, String phone, String address, String password) {

        try {

            Connection conn = ConnectToDatabase.connect();
            Statement stmt = conn.createStatement();

            String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = " + getSelectedData()[0];

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

            if (resultSetEmail.next()) {

                if (tfEmail.getText().equals(getSelectedData()[2])) {

                    preparedStatement.executeUpdate();

                } else {
                    MessageDialogs.emailAlreadyUsed(email);
                }

            } else if (email.equals("") || name.equals("") || password.equals("")) {
                MessageDialogs.enterAllFields();

            } else {
                preparedStatement.executeUpdate();
            }

            stmt.close();
            conn.close();
            connEmail.close();
            statementEmail.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUserFromDatabase(String email) {

        try {
            Connection conn = ConnectToDatabase.connect();
            Statement stmt = conn.createStatement();

            String sql = "DELETE FROM  users WHERE email = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
