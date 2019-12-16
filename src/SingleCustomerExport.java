import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.*;

/**
 * Single customer export handle exporting all of the items that a single customer receives and the quantity of each.
 */
public class SingleCustomerExport {
    private JButton exportButton;
    private JPanel contentPane;
    private JButton doneButton;
    private JTextField searchField;
    private JTable customerTable;

    private static JFrame frame = null;

    DefaultTableModel defaultModel;
    private Vector<Customer> customers;

    /**
     * Constructor. Sets up all actions regarding the JFrame.
     */
    public SingleCustomerExport() {

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchCustomers();
            }
        });

        customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SelectionChanged();
            }
        });

        //SetMatchesList(Data.DB().GetCustomersName());
        setThings();
        SelectionChanged();
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    public void Display() {
        if (frame == null) {
            frame = new JFrame("Select a Customer for Export");
            frame.setContentPane(contentPane);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
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
     * Search filter for options to add to the customers pulls.
     */
    private void searchCustomers() {
        String text = searchField.getText();
        if (text.isEmpty()) {
            SetUpCustomerTable(Data.DB().GetCustomers());
            return;
        }
        searchTable(text);
    }

    /**
     * Sets the customer table to display all customer information
     * @param newCustomers The customer vector that contains all information to display.
     */
    private void SetUpCustomerTable(Vector<Customer> newCustomers){
        String[] columns = {"ID", "Name", "Phone Number", "Email"};
        defaultModel = new DefaultTableModel(columns, 0);
        customerTable.setModel(defaultModel);
        customerTable.setDefaultEditor(Object.class, null);

        // Retrieve comics from the database and add them to the table model
        customers = Data.DB().GetCustomers();
        for(Customer c : newCustomers){
            Object[] rowData = {c.getID(), c.getName(), c.getPhone(), c.getEmail()};
            defaultModel.addRow(rowData);
        }
        customerTable.setAutoCreateRowSorter(true);
    }

    private void setThings() {
        customers = Data.DB().GetCustomers();
        SetUpCustomerTable(customers);
        customerTable.setVisible(true);
    }

    /**
     * Disables export button if there is nothing selected.
     */
    private void SelectionChanged() {
        boolean selected = !customerTable.getSelectionModel().isSelectionEmpty();
        if (!selected) {
            exportButton.setEnabled(false);
        }
        else {
            exportButton.setEnabled(true);
        }
    }

    /**
     * Dispose the window.
     */
    private void onCancel() {
        if (frame != null) { frame.dispose(); }
    }

    /**
     * Runs when the export button is pressed. Responsible for getting all required information and formatting
     * the data nicely for output.
     */
    private void onOk() {
        String customerID = customerTable.getModel().getValueAt(customerTable.getSelectedRow(), 0).toString();
        ResultSet data = Data.DB().singleCustomerExport(Data.Store(), customerID);
        boolean firstRun = true;
        Vector <ResultType> results = new Vector<>(0);
        String output = "";

        try {
            while (data.next()) {

                results.add(new ResultType(data.getString("name"), data.getString("id"),
                        data.getString("matches"), data.getString("number")));

            }
        } catch (Exception e) {
            System.err.println("Exception: "
                +e.getMessage());
        }

        int outside = results.size();
        if (outside < 1) {
            JOptionPane.showMessageDialog(null, "This customer receives no items.");
            customerTable.clearSelection();
            return;
        }

        //System.out.printf("Outside length: %d\n", outside);
        for (int i = 0; i < outside; i++) {
            //System.out.printf("Loop number: %s. Search Term: %s\n", i, results.get(i).getMatches());
            if (firstRun) {
                //System.out.println(results.get(i).getCustomerName());
                output += results.get(i).getCustomerName();
                firstRun = false;
            }

            Vector <String> titles = Data.DB().getCsvEntriesNames(Data.Store(), results.get(i).getMatches());
            int inside = titles.size();
            for (int j = 0; j < inside; j++) {
                //System.out.printf("\tTitle: %s.\n\t\tQuantity: %s\n", titles.get(j), results.get(i).getQuantity());
                output += "\tTitle: " + titles.get(i) + "\n\t\tQuantity: " + results.get(i).getQuantity() + "\n";
            }

        }
        new OutputFiles().Display(output);
        customerTable.clearSelection();
        SelectionChanged();
    }
}
