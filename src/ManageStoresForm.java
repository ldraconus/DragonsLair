import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

/**
 * Provides UI and functionality to manage stores.
 */
public class ManageStoresForm {
    private JPanel manageStoresPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton doneButton;
    private JList<String> storeList;
    private JScrollPane scrollPane;

    private static JFrame frame;

    /**
     /**
     * Return JFrame.
     * @return JFrame Returns the frame used in this class.
     */
    public static JFrame Frame() { return frame; }

    /**
     * Dispose the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    /**
     * Create a new JFrame and display the initial UI components.
     */
    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Manage Stores");
            frame.setContentPane(new ManageStoresForm().manageStoresPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    /**
     * Class constructor.
     * Setup action listeners for UI components.
     */
    public ManageStoresForm() {
        manageStoresPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) { Done(); }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) { Open(); }
        });

        storeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) { SelectionChanged(); }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { Add(); }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { Edit(); }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { Delete(); }
        });
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { Done(); }
        });
    }

    /**
     * Set the context.
     */
    private void SelectionChanged() {
        SetContext();
    }

    /**
     * Update the list of stores.
     */
    private void UpdateList() {
        storeList.clearSelection();
        Vector<String> stores = Data.DB().GetStores();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String s: stores) data.addElement(s);
        storeList.setModel(data);
    }

    /**
     * Update the store list and set the context.
     */
    private void Open() {
        storeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        UpdateList();
        SetContext();
        frame.setVisible(true);
    }

    /**
     * Set the context.
     */
    private void SetContext() {
        boolean selected = !storeList.isSelectionEmpty();
        boolean deleteable = true;
        if (selected) {
            String store = storeList.getSelectedValue();
            deleteable = !Data.DB().StoreInUse(store);
        }
        int numStores = storeList.getModel().getSize();
        editButton.setEnabled(selected);
        deleteButton.setEnabled(selected && numStores > 1 && deleteable);
        PrefsForm.setContext();
    }

    /**
     * Hide the JFrame.
     */
    private void Done() {
        frame.setVisible(false);
    }

    /**
     * Delete store.
     */
    private void Delete() {
        boolean selected = !storeList.isSelectionEmpty();
        if (selected) {
            String store = storeList.getSelectedValue();
            Message msg = new Message(Message.YesNoMessage, "Are you sure you want to delete " + store + "?");
            if (msg.getButton() == Message.NoButton) return;
            Data.DB().DeleteStore(store);
            UpdateList();
            SetContext();
        }
    }

    /**
     * Edit store.
     */
    private void Edit() {
        boolean selected = !storeList.isSelectionEmpty();
        if (!selected) return;
        String origStore = storeList.getSelectedValue();
        EditStore edit = new EditStore(origStore);
        if (EditStore.isOk()) {
            String store = EditStore.getStore();
            if (store.equals(origStore)) return;
            if (store.isEmpty()) return;
            if (Data.DB().StoreExists(store)) return;
            Data.DB().RenameStore(origStore, store);
            UpdateList();
            SetContext();
        }
    }

    /**
     * Modify store name.
     * @param str Name of the store.
     * @return String Returns new string after modifications.
     */
    private String MakeLegal(String str) {
        String newStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') newStr += '_';
            else if (str.charAt(i) != '\'' &&
                     str.charAt(i) != '\\' &&
                     str.charAt(i) != '/' &&
                     str.charAt(i) != ':' &&
                     str.charAt(i) != '"') newStr += str.charAt(i);
        }
        return newStr;
    }

    /**
     * Add new store.
     */
    private void Add() {
        AddStore add = new AddStore();
        if (AddStore.isOk()) {
            String store = AddStore.getStore();
            if (store.isEmpty()) return;
            store = MakeLegal(store);
            if (Data.DB().StoreExists(store)) return;
            Data.DB().AddStore(store);
            UpdateList();
            SetContext();
        }
    }
}
