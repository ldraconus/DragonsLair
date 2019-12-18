import javax.swing.*;
import java.awt.event.*;

/**
 * EditCustomer is responsible for editing customers information.
 */
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

    /**
     * Returns the value of ok.
     * @return true if ok is true, false otherwise.
     */
    public static boolean isOk() { return ok; }

    /**
     * Get customers name.
     * @return Name of the customer.
     */
    public static String Name() { return name; }

    /**
     * Get the customers email.
     * @return Customers email address.
     */
    public static String EMail() { return email; }

    /**
     * Get the customers phone number.
     * @return Customes phone number.
     */
    public static String Phone() { return phone; }

    /**
     * Constructor that takes in the information to be set for the given customer.
     * @param name Name to be set for the customer.
     * @param email Email to be set for the customer.
     * @param phone Phone number to be set for the customer.
     */
    public EditCustomer(String name, String email, String phone) {
        setContentPane(contentPane);
        customerNameField.setText(name);
        emailAddressField.setText(email);
        phoneNumberField.setText(phone);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        /**
         * Add action listener to OK button.
         */
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        /**
         * Add action listener to cancel button.
         */
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        /**
         * Add window listerner to call onCancel when the window is closed.
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        /**
         * Add keyboard listener to escape key to call onCancel when pressed.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /**
         * Add keyboard listener to enter key to call skipOrOK when pressed.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                skipOrOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    /**
     * SkipOfOK changes focus if there are empty fields in the form, otherwise it gets mapped to the OK button.
     */
    private void skipOrOK() {
        if (customerNameField.getText().isEmpty()) customerNameField.requestFocusInWindow();
        else if (emailAddressField.getText().isEmpty() && phoneNumberField.getText().isEmpty()) {
            if (emailAddressField.isFocusOwner()) phoneNumberField.requestFocusInWindow();
            else emailAddressField.requestFocus();
        }
        else onOK();
    }

    /**
     * Makes sure the name field and email or phone number is filled out before allowing the form to be submitted.
     */
    private void onOK() {
        name = customerNameField.getText();
        email = emailAddressField.getText();
        phone = phoneNumberField.getText();
        if (name.isEmpty() || (email.isEmpty() && phone.isEmpty())) return;
        ok = true;
        dispose();
    }

    /**
     * OnCancel closes the window.
     */
    private void onCancel() {
        ok = false;
        dispose();
    }
}
