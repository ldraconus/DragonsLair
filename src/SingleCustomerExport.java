//import com.sun.codemodel.internal.JOp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class SingleCustomerExport {
    private Vector<String> customerNames;
    private JButton exportButton;
    private JPanel contentPane;
    private JButton doneButton;
    private JTextField searchField;
    private JList customerList;

    private static JFrame frame = null;

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

        SetMatchesList(Data.DB().GetCustomers());
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
        customerNames = Data.DB().GetCustomers();
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

    private void onOk() {
        String selectedItem = customerList.getSelectedValue().toString();
        String customerID = Data.DB().getCustomerId(Data.Store(), selectedItem);
        System.out.println(customerID);
        Vector <String> pullIDs = Data.DB().getPullListIDs(Data.Store(), customerID);
        Vector <String> pullQuantities = Data.DB().getPullListQuantity(Data.Store(), customerID);
        Vector <String> pullNames = new Vector<>(0);
        Vector <String> searchTermIDs = new Vector<>(0);

        Iterator it = pullIDs.iterator();
        while (it.hasNext()) {
            String id = it.next().toString();
            pullQuantities.add(Data.DB().getPullQuantityID(Data.Store(), id));
            searchTermIDs.add(Data.DB().getSearchTermIdPullList(Data.Store(), id));
        }

        Iterator iter = searchTermIDs.iterator();
        while (iter.hasNext()) {
            String id = iter.next().toString();
            pullNames.add(Data.DB().getSearchTermIdMatches(Data.Store(), id));
        }

        int totalLength = pullIDs.size();
        if (totalLength == 0) {
            JOptionPane.showMessageDialog(null, "This person receives no items.");
            customerList.clearSelection();
            searchField.setText("");
            setCustomerNames();
            return;
        }
        ArrayList <PullRequest> items = new ArrayList <>();
        for (int i = 0; i < totalLength; i++) {
            items.add(new PullRequest(pullNames.get(i), pullQuantities.get(i)));
        }

        Collections.sort(items);

        String output = selectedItem + "'s Pulls\n\n";
        for (int i = 0; i < totalLength; i++) {
            //System.out.printf("%s", items.get(i).toString());
            output += items.get(i).toString();
        }

        new OutputFiles().Display(output);

        customerList.clearSelection();
        searchField.setText("");
        setCustomerNames();
    }
}
