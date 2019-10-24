import javax.swing.*;
import java.awt.event.*;

public class EditCustomer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField customerNameField;
    private JTextField emailAddressField;
    private JTextField phoneNumberField;
    private JButton pullListButton;

    private static boolean ok;
    private static String name;
    private static String email;
    private static String phone;
    public static boolean isOk() { return ok; }
    public static String Name() { return name; }
    public static String EMail() { return email; }
    public static String Phone() { return phone; }

    public EditCustomer(String name, String email, String phone) {
        setContentPane(contentPane);
        customerNameField.setText(name);
        emailAddressField.setText(email);
        phoneNumberField.setText(phone);
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

        pullListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPullList();
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

    }

    private void skipOrOK() {
        if (customerNameField.getText().isEmpty()) customerNameField.requestFocusInWindow();
        else if (emailAddressField.getText().isEmpty() && phoneNumberField.getText().isEmpty()) {
            if (emailAddressField.isFocusOwner()) phoneNumberField.requestFocusInWindow();
            else emailAddressField.requestFocus();
        }
        else onOK();
    }

    private void onOK() {
        name = customerNameField.getText();
        email = emailAddressField.getText();
        phone = phoneNumberField.getText();
        if (name.isEmpty() || (email.isEmpty() && phone.isEmpty())) return;
        ok = true;
        dispose();
    }

    private void onCancel() {
        ok = false;
        dispose();
    }

    private void onPullList() {
        // bring up the pull list editing dialog
    }
}
