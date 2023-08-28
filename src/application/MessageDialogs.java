package application;

import forms.admin.AdminLoginForm;
import forms.user.LoginForm;

import javax.swing.*;

public class MessageDialogs {

    public static void operationCanceled(){
        JOptionPane.showMessageDialog(null,
                "Operation canceled.",
                "Canceled",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void emailOrPasswordInvalid() {
        JOptionPane.showMessageDialog(null,
                "Email or password invalid.",
                "Try again",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void emailAlreadyUsed(String email) {
        JOptionPane.showMessageDialog(null,
                "Email " + email + " already used!",
                "Email already used",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void enterAllFields() {
        JOptionPane.showMessageDialog(null,
                "Please enter all required fields.",
                "Try again",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void dataChangedSuccessfully() {
        JOptionPane.showMessageDialog(null,
                "Data changed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void wrongAdministratorPassword() {
        JOptionPane.showMessageDialog(null,
                "Wrong administrator password!",
                "Wrong password",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void successLoginAsAdmin() {
        JOptionPane.showMessageDialog(null,
                "You successfully login as an administrator.",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
    }

}
