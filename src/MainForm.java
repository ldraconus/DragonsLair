import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

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

    public static void Display() {
        frame = new JFrame("Dragon's Lair Pull-list Actions");
        frame.setContentPane(new MainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainForm.SetContext();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
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

    private void StorePicked() {
        Data.Store((String) storeComboBox.getSelectedItem());
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
        ManageCustomersForm.Display();
    }

    private void LogOut(){
        frame.dispose();
        LoginForm.Dispose();
        PrefsForm.Dispose();
        ManageCustomersForm.Dispose();
        LoginForm.Display();
    }
}
