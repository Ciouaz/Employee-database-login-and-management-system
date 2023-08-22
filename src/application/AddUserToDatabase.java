//package application;
//
//import javax.swing.*;
//import java.sql.*;
//
//public class AddUserToDatabase {
//
//    public static User addUserToDatabase(String name, String email, String phone, String address, String password, JFrame parent) {
//        User user = null;
//
//        try {
//            Connection conn = DriverManager.getConnection(
//                    ConnectToDatabase.DB_URL,
//                    ConnectToDatabase.USERNAME,
//                    ConnectToDatabase.PASSWORD
//            );
//
//            Statement stmt = conn.createStatement();
//            String sql = "INSERT INTO users (name, email, phone, address, password) " +
//                    "VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, email);
//            preparedStatement.setString(3, phone);
//            preparedStatement.setString(4, address);
//            preparedStatement.setString(5, password);
//
//            int addedRows = preparedStatement.executeUpdate();
//
//            if (addedRows > 0) {
//                user = new User();
//                user.name = name;
//                user.email = email;
//                user.phone = phone;
//                user.address = address;
//                user.password = password;
//            }
//
//            stmt.close();
//            conn.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//            try {
//                Connection conn = DriverManager.getConnection(
//                        ConnectToDatabase.DB_URL,
//                        ConnectToDatabase.USERNAME,
//                        ConnectToDatabase.PASSWORD
//                );
//
//                PreparedStatement statement = conn.prepareStatement("SELECT email FROM users where email= ?");
//                statement.setString(1, email);
//                ResultSet resultSet = statement.executeQuery();
//
//                if (resultSet.next()) {
//                    JOptionPane.showMessageDialog(parent,
//                            "Email " + email + " already used!",
//                            "Email already used",
//                            JOptionPane.ERROR_MESSAGE
//                    );
//
//                } else {
//                    JOptionPane.showMessageDialog(parent,
//                            "Failed to register new user!",
//                            "Try again",
//                            JOptionPane.ERROR_MESSAGE
//                    );
//                }
//
//                statement.close();
//                conn.close();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//        return user;
//
//    }
//}
