import javax.swing.*;
import java.awt.event.*;

/**
 * AddStore is responsible for adding a store to the database.
 */
public class AddStore extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;

    private static String store;
    private static boolean ok;

    /**
     * Returns the value of ok.
     * @return true if ok is true, false otherwise.
     */
    public static boolean isOk() { return ok; }

    /**
     * getStore method.
     * @return Name of the store.
     */
    public static String getStore() { return store; }

    /**
     * Constructor for AddStore. Takes no input and opens the form.
     */
    public AddStore() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        /**
         * Adds action listener for the OK button.
         */
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        /**
         * Adds action listener for the cancel button.
         */
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        /**
         * Adds a listener to the window for the close button.
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        /**
         * Adds an action listener to the keyboard. Maps escape to cancel.
         */
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /**
         * Adds an action listener to the keyboard. Maps enter to onOk.
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
     * Adds the store and closes the window.
     */
    private void onOK() {
        ok = true;
        store = nameTextField.getText();
        dispose();
    }

    /**
     * Closes the window.
     */
    private void onCancel() {
        store = "";
        ok = false;
        dispose();
    }
}
