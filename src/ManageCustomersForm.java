import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

/**
 * Provides form and functionality to manage customers.
 */
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

    /**
     * Dispose of the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
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

    /**
     * Class constructor.
     * Setup action listeners for UI components.
     */
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

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                /* ignore */                
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                /* ignore */
                searchCustomers();
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

    /**
     * Search filter
     */
    private void searchCustomers() {
        String text = searchTextField.getText();
        if (text.isEmpty()) {
            SetCustomerList(customers);
            SetContext();
            return;
        }
        Vector<String> filtered = new Vector<String>();
        for (String c: customers) if (c.toLowerCase().contains(text.toLowerCase())) filtered.addElement(c);
        SetCustomerList(filtered);
        SetContext();
    }

    /**
     * Enable or disable add, delete, and edit.
     */
    private void SetContext() {
        addButton.setEnabled(true);
        deleteButton.setEnabled(!customerList.isSelectionEmpty());
        editButton.setEnabled(!customerList.isSelectionEmpty());
    }

    /**
     * Filter the customer list.
     * @param cust Used for filtering.
     */
    private void SetCustomerList(Vector<String> cust) {
        customerList.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: cust) data.addElement(c);
        customerList.setModel(data);
    }

    /**
     * Get and set the customer list.
     */
    private void Open() {
        //customerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customers = Data.DB().GetCustomers();
        SetCustomerList(customers);
        SetContext();
        frame.setVisible(true);
    }

    /**
     * Add new customer.
     */
    private void Add() {
        new AddCustomer();
        if (AddCustomer.isOk()) {
            String name = AddCustomer.Name();
            if (Data.DB().CustomerExists(Data.Store(), name)) return;
            String email = AddCustomer.EMail();
            String phone = AddCustomer.Phone();
            Data.DB().AddCustomer(Data.Store(), name, email, phone);
            searchTextField.setText("");
            customers = Data.DB().GetCustomers();
            SetCustomerList(customers);
            SetContext();
        }
    }

    /**
     * Edit customer.
     */
    private void Edit() {
        boolean selected = !customerList.isSelectionEmpty();
        if (!selected) return;
        String origCustomer = customerList.getSelectedValue();
        String email = Data.DB().GetCustomerEMail(Data.Store(), origCustomer);
        String phone = Data.DB().GetCustomerPhone(Data.Store(), origCustomer);
        new EditCustomer(origCustomer, email, phone);
        if (EditCustomer.isOk()) {
            String customer = EditCustomer.Name();
            if (customer.equals(origCustomer)) return;
            if (customer.isEmpty()) return;
            if (Data.DB().CustomerExists(Data.Store(), customer)) return;
            Data.DB().UpdateCustomer(Data.Store(), origCustomer, customer, email, phone);
            searchTextField.setText("");
            customers = Data.DB().GetCustomers();
            SetCustomerList(customers);
            SetContext();
        }
    }

    /**
     * Delete customer.
     */
    private void Delete() {
        boolean selected = !customerList.isSelectionEmpty();
        if (!selected) return;
        String origCustomer = customerList.getSelectedValue();
        Message msg = new Message(Message.YesNoMessage, "Are you sure you want to delete " + origCustomer + "?");
        if (msg.getButton() == Message.NoButton) return;
        Data.DB().DeleteCustomer(Data.Store(), origCustomer);
        customers = Data.DB().GetCustomers();
        SetCustomerList(customers);
        SetContext();
    }

    /**
     * Hide the frame.
     */
    //private void Done() {frame.setVisible(false);}
    private void Done() {frame.dispose();}

    /**
     * Set the context.
     */
    private void SelectionChanged() {
        SetContext();
    }
}
