import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

public class ManageCustomersForm {
    private JList<String> customerList;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton doneButton;
    private JTextField searchTextField;
    private JPanel manageCustomersPanel;

    private static JFrame frame = null;

    private Vector<String> customers;

    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Manage Customers");
            frame.setContentPane(new ManageCustomersForm().manageCustomersPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    public ManageCustomersForm() {
        manageCustomersPanel.registerKeyboardAction(new ActionListener() {
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

        customerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) { SelectionChanged(); }
        });
        
        searchTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                searchCustomers();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                /* ignore */                
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                /* ignore */
            }
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

    private void searchCustomers() {
        String text = searchTextField.getText();
        if (text.isEmpty()) {
            SetCustomerList(customers);
            SetContext();
            return;
        }
        Vector<String> filtered = new Vector<String>();
        for (String c: customers) if (c.contains(text)) filtered.addElement(c);
        SetCustomerList(filtered);
        SetContext();
    }

    private void SetContext() {
        addButton.setEnabled(true);
        deleteButton.setEnabled(!customerList.isSelectionEmpty());
        editButton.setEnabled(!customerList.isSelectionEmpty());
    }

    private void SetCustomerList(Vector<String> cust) {
        customerList.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: cust) data.addElement(c);
        customerList.setModel(data);
    }

    private void Open() {
        customerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customers = Data.DB().GetCustomers();
        SetCustomerList(customers);
        SetContext();
        frame.setVisible(true);
    }

    private void Add() {

    }

    private void Edit() {

    }

    private void Delete() {

    }

    private void Done() {
        frame.setVisible(false);
    }

    private void SelectionChanged() {
        SetContext();
    }
}
