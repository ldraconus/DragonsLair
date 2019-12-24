import javax.swing.*;
import java.awt.event.*;

/**
 * EditStore is responsible for editing the store information.
 */
public class EditStore extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;

    private static String store;
    private static boolean ok;

    /**
     * Returns the value of ok.
     * @return True if ok is true, false otherwise.
     */
    public static boolean isOk() { return ok; }

    /**
     * Get the name of the store.
     * @return The name of the store.
     */
    public static String getStore() { return store; }

    /**
     * Changes the name of the store.
     * @param origName The original name of the store.
     */
    public EditStore(String origName) {
        setContentPane(contentPane);
        setModal(true);
        nameTextField.setText(origName);
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
         * Add window listener to the close button. Calls onCancel.
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        /**
         * Add keyboard listener to escape key. Calls onCancel.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /**
         * Add keyboard listener to enter key. Calls onOK.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * onOk says it is ok to update the information and closes the window.
     */
    private void onOK() {
        ok = true;
        store = nameTextField.getText();
        dispose();
    }

    /**
     * onCancel closes the window and does not allow changing the name of the store.
     */
    private void onCancel() {
        store = "";
        ok = false;
        dispose();
    }
}
