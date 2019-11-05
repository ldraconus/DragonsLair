import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * AddUser is responsible for adding a user to the database.
 */
public class AddUser extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField userNameTextField;
    private JPasswordField passwordField;
    private JComboBox<String> storeComboBox;

    private static String user;
    private static String password;
    private static String store;
    private static boolean ok;

    /**
     * Gets the value of ok.
     * @return True if ok is true, false otherwise.
     */
    public static boolean isOk() { return ok; }

    /**
     * GetStore.
     * @return Store Name.
     */
    public static String getStore() { return store; }

    /**
     * GetPassword.
     * @return User password.
     */
    public static String getPassword() { return password; }

    /**
     * getUser.
     * @return User name.
     */
    public static String getUser() { return user; }

    /**
     * Constructor that takes no parameters.
     */
    public AddUser()
    {
        this("", null);
    }

    /**
     * Constructor that take parameters. This is the recommended way to call AddUser.
     * @param u Name of the user.
     * @param s Name of the store the user is connected to.
     */
    public AddUser(String u, String s) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        /**
         * Adds action listener to OK button.
         */
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        /**
         * Adds action listener to Cancel button.
         */
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        /**
         * Adds window listener to close button. Calls onCancel.
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        /**
         * Adds keyboard listener to the escape key, calls onCancel.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /**
         * Adds keyboard listener to the enter key.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        SetStores();
        if (s != null && !s.isEmpty()) storeComboBox.setSelectedItem(s);
        userNameTextField.setText(u);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * Gets the name of all the stores and sets them as options for the drop down menu.
     */
    private void SetStores()
    {
        Vector<String> stores = Data.DB().GetStores();
        DefaultComboBoxModel<String> data = new DefaultComboBoxModel<String>();
        for (String s: stores) data.addElement(s);
        storeComboBox.setModel(data);
    }

    /**
     * Runs when OK is selected. Sets user information if valid.
     */
    private void onOK() {
        ok = true;
        user = userNameTextField.getText();
        char[] pass = passwordField.getPassword();
        String p = "";
        for (char ch: pass) p += ch;
        if (!p.equals("")) password = p;
        store = ((String) storeComboBox.getSelectedItem());
        dispose();
    }

    /**
     * Runs when closing the window.
     */
    private void onCancel() {
        store = "";
        user = "";
        password = "";
        ok = false;
        dispose();
    }
}
