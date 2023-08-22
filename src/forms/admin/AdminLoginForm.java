package forms.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginForm extends JFrame {
    private static final String ADMIN_PASSWORD = "admin";
    private JPasswordField pfAdminPassword;
    private JButton btnAdminLogin;
    private JButton btnAdminCancel;
    private JPanel adminLoginPanel;

    public AdminLoginForm(JFrame parent) {
        setTitle("Administrator login");
        setContentPane(adminLoginPanel);
        setMinimumSize(new Dimension(500, 550));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnAdminLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String password = String.valueOf(pfAdminPassword.getPassword());

                if (password.equals(ADMIN_PASSWORD)) {

                    JOptionPane.showMessageDialog(AdminLoginForm.this,
                            "You successfully login as an administrator",
                            "Successful login as an administrator",
                            JOptionPane.PLAIN_MESSAGE);

                    dispose();
                    new AdminForm(AdminLoginForm.this);

                } else {
                    JOptionPane.showMessageDialog(AdminLoginForm.this,
                            "Wrong administrator password!",
                            "Wrong password",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnAdminCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminLoginForm.this,
                        "Operation canceled",
                        "Canceled",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        });

        setVisible(true);
    }
}
