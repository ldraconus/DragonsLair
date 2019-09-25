import javax.swing.*;
import java.awt.event.*;

public class MainForm {
    private JPanel MainPanel;
    private JButton manageCustomersButton;
    private JButton manageItemsButton;
    private JComboBox storeComboBox;
    private JButton importCSVButton;
    private JButton printReportsButton;
    private JButton editPreferencesButton;

    private static JFrame frame = null;

    public static void Display() {
        frame = new JFrame("Dragon's Lair Pull-list Actions");
        frame.setContentPane(new MainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public MainForm() {
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
    }

    private void SetContext() {
        boolean flag = !Data.Store().isEmpty();
        manageCustomersButton.setEnabled(flag);
        manageItemsButton.setEnabled(flag);
        storeComboBox.setEnabled(flag);
        importCSVButton.setEnabled(flag);
        printReportsButton.setEnabled(flag);
        editPreferencesButton.setEnabled(true);
        // get a vector of stores from the DB
        // set the combo box to this value, if any
        // set the default value of the combo box to the users default
        // set the default store in the Data
    }

    private void StorePicked() {
        // get which store was selected
        // set the default store in the data
    }

    private void EditPreferences() {
        PrefsForm.Display();
    }

    private void PrintReports() {
    }

    private void ImportCSV() {
    }

    private void ManageItems() {
    }

    private void ManageCustomers() {
    }
}
