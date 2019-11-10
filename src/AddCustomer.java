import javax.swing.*;
import java.awt.event.*;

public class AddCustomer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField customerNameTextField;
    private JTextField emailAddressTextField;
    private JTextField phoneNumberTextField;

    private static boolean ok;
    private static String name;
    private static String email;
    private static String phone;

    public static String Name() { return name; }
    public static String EMail() { return email; }
    public static String Phone() { return phone; }
    public static boolean isOk() { return ok; }

    public AddCustomer() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call skipOrOK() on ENTER
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                skipOrOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        customerNameTextField.setText(name);
        phoneNumberTextField.setText(phone);
        emailAddressTextField.setText(email);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void skipOrOK() {
        if (customerNameTextField.getText().isEmpty()) customerNameTextField.requestFocusInWindow();
        else if (emailAddressTextField.getText().isEmpty() && phoneNumberTextField.getText().isEmpty()) {
            if (emailAddressTextField.isFocusOwner()) phoneNumberTextField.requestFocusInWindow();
            else emailAddressTextField.requestFocus();
        }
        else onOK();
    }

    private void onOK() {
        name = customerNameTextField.getText();
        email = emailAddressTextField.getText();
        phone = phoneNumberTextField.getText();
        if (name.isEmpty() || (email.isEmpty() && phone.isEmpty())) return;
        ok = true;
        dispose();
    }

    private void onCancel() {
        ok = false;
        dispose();
    }
}
