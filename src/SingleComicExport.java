import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;

/**
 * This is the window that opens when you want to export everybody that gets a single comic.
 * Note: This window does not currently work.
 */
public class SingleComicExport extends JDialog{
    private Vector<String> possiblePulls;
    private JPanel contentPane;
    private JList inInventory;
    private JTextField searchField;
    private JButton okButton;
    private JButton cancelButton;

    private static JFrame frame = null;

    /**
     * Sets up all information for the window.
     */
    public SingleComicExport() {

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchPullOptions();
            }
        });

        inInventory.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { SelectionChanged(); }
        });

        SetMatchesList(Data.DB().getCsvEntries());
        SelectionChanged();
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    public void Display() {
        if (frame == null) {
            frame = new JFrame("Select a Comic for Export");
            frame.setContentPane(contentPane);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    /**
     * Filter the customer list.
     * @param comic Used for filtering.
     */
    private void SetMatchesList(Vector<String> comic) {
        inInventory.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: comic) data.addElement(c);
        inInventory.setModel(data);
    }

    /**
     * Sets the list of possible pulls that could be added.
     */
    private void setPossiblePulls() {
        possiblePulls = Data.DB().getCsvEntries();
    }

    /**
     * Search filter for options to add to the customers pulls.
     */
    private void searchPullOptions() {
        String text = searchField.getText();
        if (text.isEmpty()) {
            setPossiblePulls();
            SetMatchesList(possiblePulls);
            return;
        }
        setPossiblePulls();
        Vector<String> filtered = new Vector<String>();
        for (String c: possiblePulls) if (c.toLowerCase().contains(text.toLowerCase())) filtered.addElement(c);
        SetMatchesList(filtered);
    }

    /**
     * Disables the ok button if nothing is selected.
     */
    private void SelectionChanged() {
        boolean selected = !inInventory.isSelectionEmpty();
        if (!selected) {
            okButton.setEnabled(false);
        }
        else {
            okButton.setEnabled(true);
        }
    }

    /**
     * Runs when ok button is pressed. Validates selection before trying to save it to file.
     */
    private void onOk() {
        String selectedItem = inInventory.getSelectedValue().toString();
        //System.out.println(selectedItem);
        String searchID = Data.DB().getSearchTermIdPullListSingleComic(Data.Store(), selectedItem);
        //System.out.println(searchID);
        Vector <String> customerIDs = Data.DB().getPullListCustomerId(Data.Store(), searchID);
        Vector <String> customerNames = new Vector<>(0);
        Iterator it = customerIDs.iterator();
        while (it.hasNext()) {
            String id = it.next().toString();
            //System.out.println(Data.DB().GetCustomerName(Data.Store(), id));
            customerNames.add(Data.DB().GetCustomerName(Data.Store(), id));
        }

        int customerCount = customerNames.size();
        if (customerCount == 0) {
            JOptionPane.showMessageDialog(null, "Nobody receives this item.");
            return;
        }

        String outputData = selectedItem + "\n";
        //System.out.println(selectedItem);
        for (int in = 0; in < customerCount; in++) {
            //System.out.println("\t" + customerNames.get(in) + "\n\t\t" + Data.DB().getPullQuantity(Data.Store(), customerIDs.get(in), searchID));
            outputData += "\t" + customerNames.get(in) + "\n\tRequested Quantity: " + Data.DB().getPullQuantity(Data.Store(), customerIDs.get(in), searchID) + "\n\n";
        }

        new OutputFiles().Display(outputData);

        inInventory.clearSelection();
        searchField.setText("");
        searchPullOptions();
    }

    /**
     * Dispose the window.
     */
    private void onCancel() {
        if (frame != null) { frame.dispose(); }
    }
}
