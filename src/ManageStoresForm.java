import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

public class ManageStoresForm {
    private JPanel manageStoresPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton doneButton;
    private JList<String> storeList;
    private JScrollPane scrollPane;

    private static JFrame frame;
    public static JFrame Frame() { return frame; }
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Manage Storess");
            frame.setContentPane(new ManageStoresForm().manageStoresPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

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

    private void SelectionChanged() {
        SetContext();
    }

    private void UpdateList() {
        storeList.clearSelection();
        Vector<String> stores = Data.DB().GetStores();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String s: stores) data.addElement(s);
        storeList.setModel(data);
    }

    private void Open() {
        storeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        UpdateList();
        SetContext();
        frame.setVisible(true);
    }

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
    }

    private void Done() {
        frame.setVisible(false);
    }

    private void Delete() {
        boolean selected = !storeList.isSelectionEmpty();
        if (selected) {
            String store = storeList.getSelectedValue();
            Data.DB().DeleteStore(store);
            UpdateList();
            SetContext();
        }
    }

    private void Edit() {
        // Display filled out EditStore dialog
    }

    private void Add() {
        AddStore add = new AddStore();
        if (add.isOk()) {
            String name = add.getStore();
            if (name.isEmpty()) return;
            if (Data.DB().StoreExists(name)) return;
            Data.DB().AddStore(name);
            UpdateList();
            SetContext();
        }
    }
}
