package forms.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
                login();
            }
        });
        btnAdminCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               cancel();
            }
        });

        KeyStroke keystrokeCancel = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        adminLoginPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystrokeCancel, "ESCAPE");
        adminLoginPanel.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        KeyStroke keystrokeLogin = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        adminLoginPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystrokeLogin, "ENTER");
        adminLoginPanel.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login(){
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
    private void cancel(){
        JOptionPane.showMessageDialog(AdminLoginForm.this,
                "Operation canceled",
                "Canceled",
                JOptionPane.ERROR_MESSAGE);
        dispose();
    }


}
