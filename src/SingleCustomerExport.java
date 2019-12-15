//import com.sun.codemodel.internal.JOp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private Vector<String> customerNames;
    private JButton exportButton;
    private JPanel contentPane;
    private JButton doneButton;
    private JTextField searchField;
    private JList customerList;

    private static JFrame frame = null;

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
                searchCustomerNames();
            }
        });

        customerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { SelectionChanged(); }
        });

        SetMatchesList(Data.DB().GetCustomersName());
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
     * Filter the customer list.
     * @param customer Used for filtering.
     */
    private void SetMatchesList(Vector<String> customer) {
        customerList.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: customer) data.addElement(c);
        customerList.setModel(data);
    }

    /**
     * Sets the list of customerNames.
     */
    private void setCustomerNames() {
        customerNames = Data.DB().GetCustomersName();
    }

    /**
     * Search filter for options to add to the customers pulls.
     */
    private void searchCustomerNames() {
        String text = searchField.getText();
        if (text.isEmpty()) {
            setCustomerNames();
            SetMatchesList(customerNames);
            return;
        }
        setCustomerNames();
        Vector<String> filtered = new Vector<String>();
        for (String c: customerNames) if (c.toLowerCase().contains(text.toLowerCase())) filtered.addElement(c);
        SetMatchesList(filtered);
    }

    /**
     * Disables export button if there is nothing selected.
     */
    private void SelectionChanged() {
        boolean selected = !customerList.isSelectionEmpty();
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
        String selectedItem = customerList.getSelectedValue().toString();
        String customerID = Data.DB().getCustomerId(Data.Store(), selectedItem);
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
        //System.out.printf("Outside length: %d\n", outside);
        for (int i = 0; i < outside; i++) {
            //System.out.printf("Loop number: %s. Search Term: %s\n", i, results.get(i).getMatches());
            if (firstRun) {
                System.out.println(results.get(i).getCustomerName());
                output += results.get(i).getCustomerName();
                firstRun = false;
            }

            Vector <String> titles = Data.DB().getCsvEntriesNames(Data.Store(), results.get(i).getMatches());
            int inside = titles.size();
            for (int j = 0; j < inside; j++) {
                System.out.printf("\tTitle: %s.\n\t\tQuantity: %s\n", titles.get(j), results.get(i).getQuantity());
                output += "\tTitle: " + titles.get(i) + "\n\t\tQuantity: " + results.get(i).getQuantity() + "\n";
            }

        }
        new OutputFiles().Display(output);
        customerList.clearSelection();
        SelectionChanged();
    }
}
