package forms.user;

import javax.swing.*;
import java.awt.*;

public class UserForm extends JFrame{
    private JPanel UserFormPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton updateDataButton;
    private JButton delateAccountButton;

    public UserForm(JFrame parent) {
        setTitle("User panel");
        setContentPane(UserFormPanel);
        setMinimumSize(new Dimension(500,550));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);

    }

    public static void main(String[] args) {
        UserForm userForm = new UserForm(null);
    }

}
