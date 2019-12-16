import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Vector;

/**
 * Provides form and functionality to manage customers.
 */
public class ManageCustomersForm {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton doneButton;
    private JTextField searchTextField;
    private JPanel manageCustomersPanel;
    private JTable customerTable;

    private static JFrame frame = null;

    DefaultTableModel defaultModel;
    private Vector<Customer> customers;

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

        customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SetContext();
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
     * Search filter for options to add to the customers pulls.
     */
    private void searchCustomers() {
        String text = searchTextField.getText();
        if (text.isEmpty()) {
            SetUpCustomerTable(Data.DB().GetCustomers());
            return;
        }
        searchTable(text);
    }

    /**
     * Searches the table based on the string passed to the function.
     * @param searchString The string to search the entire table for.
     */
    private void searchTable(String searchString){
        Vector<Customer> filtered = new Vector<>();
        for (Customer c : customers) {
            String searchThing = searchString.toLowerCase();
            if(c.getName().toLowerCase().contains(searchThing) || c.getID().toLowerCase().contains(searchThing) ||
                    c.getEmail().toLowerCase().contains(searchThing) ||
                    c.getPhone().toLowerCase().contains(searchThing)){
                filtered.addElement(c);
            }
        }

        SetUpCustomerTable(filtered);
    }

    /**
     * Enable or disable add, delete, and edit.
     */
    private void SetContext() {
        addButton.setEnabled(true);
        deleteButton.setEnabled(!customerTable.getSelectionModel().isSelectionEmpty());
        editButton.setEnabled(!customerTable.getSelectionModel().isSelectionEmpty());
    }

    /**
     * Sets the customer table to contain all information.
     * @param newCustomers the vector of customer objects to display.
     */
    private void SetUpCustomerTable(Vector<Customer> newCustomers){
        String[] columns = {"ID","Name", "Phone Number", "Email"};
        defaultModel = new DefaultTableModel(columns, 0);
        customerTable.setModel(defaultModel);
        customerTable.setDefaultEditor(Object.class, null);

        // Retrieve customers from the database and add them to the table model
        customers = Data.DB().GetCustomers();
        for(Customer c : newCustomers){
            Object[] rowData = {c.getID(), c.getName(), c.getPhone(), c.getEmail()};
            defaultModel.addRow(rowData);
        }
    }

    /**
     * Get and set the customer list.
     */
    private void Open() {
        customers = Data.DB().GetCustomers();
        SetUpCustomerTable(customers);
        customerTable.setVisible(true);

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
            String email = AddCustomer.EMail();
            String phone = AddCustomer.Phone();
            if (Data.DB().CustomerExists(Data.Store(), name, email, phone)) return;
            Data.DB().AddCustomer(Data.Store(), name, email, phone);
            searchTextField.setText("");
            customers = Data.DB().GetCustomers();
            SetUpCustomerTable(customers);
            SetContext();
        }
    }

    /**
     * Edit customer.
     */
    private void Edit() {
        if(customerTable.getSelectionModel().isSelectionEmpty())
            return;
        int selectedRow = customerTable.getSelectedRow();
        String id = customerTable.getModel().getValueAt(selectedRow, 0).toString();
        String name = Data.DB().GetCustomerName(Data.Store(), id);
        String email = Data.DB().GetCustomerEMail(Data.Store(), id);
        String phone = Data.DB().GetCustomerPhone(Data.Store(), id);
        new EditCustomer(name, email, phone);
        if (EditCustomer.isOk()) {
            String newName = EditCustomer.Name();
            String newPhone = EditCustomer.Phone();
            String newEmail = EditCustomer.EMail();
            if (newName.equals(name) && newPhone.equals(phone) && newEmail.equals(email)) return;
            if (newName.isEmpty() || newPhone.isEmpty() && newEmail.isEmpty()) return;
            if (Data.DB().CustomerExists(Data.Store(), newName, newEmail, newPhone)) return;
            Data.DB().UpdateCustomer(Data.Store(), id, newName, newEmail, newPhone);
            searchTextField.setText("");
            customers = Data.DB().GetCustomers();
            SetUpCustomerTable(customers);
            customerTable.clearSelection();
            SetContext();
        }
        customerTable.clearSelection();
        SetContext();
    }

    /**
     * Delete customer.
     */
    private void Delete() {
        if(customerTable.getSelectionModel().isSelectionEmpty())
            return;

        int selectedRow = customerTable.getSelectedRow();
        String customerID = customerTable.getModel().getValueAt(selectedRow, 0).toString();
        String customerName = customerTable.getModel().getValueAt(selectedRow, 1).toString();
        Message msg = new Message(Message.YesNoMessage, "Are you sure you want to delete " + customerName + "?");

        if (msg.getButton() == Message.NoButton) return;

        Data.DB().DeleteCustomer(Data.Store(), customerID);
        customers = Data.DB().GetCustomers();
        SetUpCustomerTable(customers);
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
