import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

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
    public static boolean isOk() { return ok; }
    public static String getStore() { return store; }
    public static String getPassword() { return password; }
    public static String getUser() { return user; }

    public AddUser()
    {
        this("", null);
    }

    public AddUser(String u, String s) {
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

        // call onOK() on ENTER
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

    private void SetStores()
    {
        Vector<String> stores = Data.DB().GetStores();
        DefaultComboBoxModel<String> data = new DefaultComboBoxModel<String>();
        for (String s: stores) data.addElement(s);
        storeComboBox.setModel(data);
    }

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

    private void onCancel() {
        store = "";
        user = "";
        password = "";
        ok = false;
        dispose();
    }
}
