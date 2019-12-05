import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Set;
import java.util.Vector;

/**
 * Manage customer pull list.
 */
public class PullLIst extends JDialog {
    private Vector<String> customerPullsList;
    private Vector<String> possiblePulls;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField requestSearch;
    private JTextField customerPullsSearch;
    private JButton addToPullButton;
    private JButton addItemButton;
    private JButton removeButton;
    private JList inInventory;
    private JList customerPulls;
    private String name;
    private String email;
    private String phone;

    private static JFrame frame = null;

    /**
     * Class constructor.
     * Setup action listeners for UI components.
     */
    public PullLIst(String nameIn, String emailIn, String phoneIn) {
        name = nameIn;
        email = emailIn;
        phone = phoneIn;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });


        customerPulls.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { pullListSelectionChanged(); }
        });

        inInventory.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectionChanged();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        SetMatchesList(Data.DB().getSearchTermsNames());
        SetPullsList(Data.DB().getSearchTermNameVector(Data.Store(), Data.DB().getCustomerID(name, phone, email)));

        addToPullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToPull();
            }
        });

        requestSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchPullOptions();
            }
        });

        customerPullsSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent c) {
                searchCustomerPullOptions();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteItem();
            }
        });
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    public void Display(String name1, String email1, String phone1) {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Manage Pull List");
            frame.setContentPane(new PullLIst(name1, email1, phone1).contentPane);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    /**
     * Still working on.
     */
    private void onOK() {
        // add your code here
        dispose();
    }

    /**
     * Still working on.
     */
    private void onCancel() {
        // add your code here if necessary
        Dispose();
    }

    /**
     * Add Search term window.
     */
    private void addItem() {
        new AddSearchTerm().Display();
    }

    /**
     * Adds selected item from in inventory list to customers pull list. Quantity is always one as of now.
     */
    private void addToPull() {
        String cuid = Data.DB().getCustomerID(name, phone, email);
        boolean selected = !inInventory.isSelectionEmpty();
        if (!selected) return;
        String searchID = Data.DB().getSearchTermId(Data.Store(), inInventory.getSelectedValue().toString());
        Data.DB().insertPullList(Data.Store(), cuid, searchID, "1");
        setPulls();
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
     * Filter the customer list.
     * @param comic Used for filtering.
     */
    private void SetPullsList(Vector<String> comic) {
        customerPulls.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: comic) data.addElement(c);
        customerPulls.setModel(data);
    }

    /**
     * Updates the customer pull list window
     */
    private void setPulls() {
        customerPulls.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customerPullsList = Data.DB().getSearchTermNameVector(Data.Store(), Data.DB().getCustomerID(name, phone, email));
        SetPullsList(customerPullsList);
        frame.setVisible(true);
    }

    /**
     * Removes an item from the customers pull list if an item is selected.
     */
    private void deleteItem() {
        int selectedPosition = customerPulls.getSelectedIndex();
        Vector<String> pullIds = Data.DB().getPullCustomerId(Data.Store(), Data.DB().getCustomerID(name, phone, email));
        Data.DB().deletePullList(Data.Store(), pullIds.get(selectedPosition));
        setPulls();
        customerPulls.clearSelection();
        pullListSelectionChanged();
    }

    /**
     * Search filter for options to add to the customers pulls.
     */
    private void searchPullOptions() {
        String text = requestSearch.getText();
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
     * Search filter for pulls a customer currently has.
     */
    private void searchCustomerPullOptions() {
        String text = customerPullsSearch.getText();
        if (text.isEmpty()) {
            setPulls();
            return;
        }
        Vector<String> filtered = new Vector<>();
        for (String c: customerPullsList) if (c.toLowerCase().contains(text.toLowerCase())) filtered.addElement(c);
        SetPullsList(filtered);
    }

    /**
     * Dispose of the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        frame.dispose();
    }

    /**
     * Deselects everything from the current customer pulls side.
     */
    private void selectionChanged() {
        customerPulls.clearSelection();
    }

    /**
     * Sets the list of possible pulls that could be added.
     */
    private void setPossiblePulls() {
        possiblePulls = Data.DB().getSearchTermsNames();
    }

    /**
     * Clears the selection on the customer side and enables or disables the remove button depending on a selection.
     */
    private void pullListSelectionChanged() {
        inInventory.clearSelection();
        boolean selected = !customerPulls.isSelectionEmpty();
        if (!selected) {
            removeButton.setEnabled(false);
        }
        else {
            removeButton.setEnabled(true);
        }
    }

}
