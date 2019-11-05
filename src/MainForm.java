import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Main form of the application.
 */
public class MainForm {
    private JPanel MainPanel;
    private JButton manageCustomersButton;
    private JButton manageItemsButton;
    private JComboBox<String> storeComboBox;
    private JButton importCSVButton;
    private JButton printReportsButton;
    private JButton editPreferencesButton;
    private JButton logout;

    private static MainForm mainForm = null;
    private static JFrame frame = null;

    /**
     * Create a new JFrame and display the initial UI components.
     */
    public static void Display() {
        frame = new JFrame("Dragon's Lair Pull-list Actions");
        frame.setContentPane(new MainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainForm.SetContext();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Constructor of this class.
     * Setup action listeners to UI components.
     */
    public MainForm() {
        mainForm = this;
        MainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) { SetContext(); }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (LoginForm.CloseYesNo()) {
                    frame.dispose();
                    LoginForm.Dispose();
                    PrefsForm.Dispose();
                    ManageCustomersForm.Dispose();
                }
            }
        });

        storeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { StorePicked(); }
        });

        manageCustomersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { ManageCustomers(); }
        });
        manageItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { ManageItems(); }
        });
        importCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { ImportCSV(); }
        });
        printReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { PrintReports(); }
        });

        editPreferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { EditPreferences(); }
        });

        logout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) { LogOut(); }
        });
    }

    /**
     * Enable or disable form buttons based on if the data store is empty or not.
     */
    private void SetContext() {
        boolean flag = !Data.Store().isEmpty();
        manageCustomersButton.setEnabled(flag);
        manageItemsButton.setEnabled(flag);
        storeComboBox.setEnabled(flag);
        importCSVButton.setEnabled(flag);
        printReportsButton.setEnabled(flag);
        editPreferencesButton.setEnabled(true);
        logout.setEnabled(true);
        Vector<String> stores = Data.DB().GetStores();
        DefaultComboBoxModel<String> data = new DefaultComboBoxModel<String>();
        for (String s : stores) data.addElement(s);
        storeComboBox.setModel(data);
        if (!Data.Store().isEmpty()) storeComboBox.setSelectedItem(Data.Store());
    }

    /**
     * Set the Data Store.
     */
    private void StorePicked() {
        Data.Store((String) storeComboBox.getSelectedItem());
    }

    /**
     * Display the Preferences Form.
     */
    private void EditPreferences() {
        PrefsForm.Display();
    }

    /**
     * FUTURE: Display the Print Reports Form.
     */
    private void PrintReports() {
    }

    /**
     * FUTURE: Display the CSV Form.
     */
    private void ImportCSV() {CSVForm.Display();  }

    /**
     * FUTURE: Display the Manage Comics Form.
     */
    private void ManageItems() {
    }

    /**
     * Display the Manage Customers Form.
     */
    private void ManageCustomers() {
        ManageCustomersForm.Display();
    }

    /**
     * Dispose of all forms and then display the Login Form.
     */
    private void LogOut(){
        frame.dispose();
        LoginForm.Dispose();
        PrefsForm.Dispose();
        ManageCustomersForm.Dispose();
        LoginForm.Display();
    }
}
