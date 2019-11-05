import javax.swing.*;
import java.awt.event.*;

/**
 * AddCustomer is responsible for adding a customer and their information to the database.
 */
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

    /**
     * Get customer name.
     * @return Name of the customer.
     */
    public static String Name() { return name; }

    /**
     * Get customer email.
     * @return Email of the customer.
     */
    public static String EMail() { return email; }

    /**
     * Get customer phone number.
     * @return Phone number of the customer.
     */
    public static String Phone() { return phone; }

    /**
     * Get the value of ok.
     * @return true if ok is true, false otherwise.
     */
    public static boolean isOk() { return ok; }

    /**
     * Constructor for AddCustomer. Takes in no information and sets up the form.
     */
    public AddCustomer() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        /**
         * Action listener for OK button.
         */
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        /**
         * Action listener for cancel button.
         */
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        /**
         * Window listener for when the window closes. Calls onCancel.
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        /**
         * Keyboard Action listener for Escape key press. Calls onCancel.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /**
         * Keyboard action listener for Enter key. Calls skipOrOK();
         */
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

    /**
     * SkipOfOK changes focus if there are empty fields in the form, otherwise it gets mapped to the OK button.
     */
    private void skipOrOK() {
        if (customerNameTextField.getText().isEmpty()) customerNameTextField.requestFocusInWindow();
        else if (emailAddressTextField.getText().isEmpty() && phoneNumberTextField.getText().isEmpty()) {
            if (emailAddressTextField.isFocusOwner()) phoneNumberTextField.requestFocusInWindow();
            else emailAddressTextField.requestFocus();
        }
        else onOK();
    }

    /**
     * Makes sure the name field and email or phone number is filled out before allowing the form to be submitted.
     */
    private void onOK() {
        name = customerNameTextField.getText();
        email = emailAddressTextField.getText();
        phone = phoneNumberTextField.getText();
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
