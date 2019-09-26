import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

public class ManageUsersForm {
    private JList<String> userList;
    private JPanel manageUsersPanel;
    private JButton deleteButton;
    private JButton editButton;
    private JButton addButton;
    private JButton doneButton;
    private JScrollPane scrollPane;

    private static JFrame frame;
    public static JFrame Frame() { return frame; }
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Manage Users");
            frame.setContentPane(new ManageUsersForm().manageUsersPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    public ManageUsersForm() {
        manageUsersPanel.registerKeyboardAction(new ActionListener() {
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

        userList.addListSelectionListener(new ListSelectionListener() {
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

    private void Open() {
        userList.clearSelection();
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Vector<String> users = Data.DB().GetUsers();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String s: users) data.addElement(s);
        userList.setModel(data);
        SetContext();
        frame.setVisible(true);
    }

    private void SetContext() {
        boolean flag = !userList.isSelectionEmpty();
        int numUsers = userList.getModel().getSize();
        int numStores = Data.DB().GetStores().size();
        editButton.setEnabled(flag && numStores > 0);
        deleteButton.setEnabled(flag && numUsers > 1);
    }

    private void Done() {
        frame.setVisible(false);
    }

    private void Delete() {
        // remove selected index
    }

    private void Edit() {
        // Display filled out EditUser dialog
    }

    private void Add() {
        // Display Empty AddUser dialog
    }
}
