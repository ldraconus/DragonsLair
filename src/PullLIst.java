import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

/**
 * Manage customer pull list.
 */
public class PullLIst extends JDialog {
    private Vector<String> possibleMatches;
    private Vector<String> customerRequests;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton addToPullButton;
    private JButton addItemButton;
    private JButton removeButton;
    private JRadioButton nameButton;
    private JRadioButton issueButton;
    private JRadioButton diamondButton;
    private JRadioButton graphicButton;
    private JRadioButton nonBookButton;
    private JList inInventory;
    private JList customerPulls;
    public String customerID;
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

        inInventory.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) { SelectionChanged(); }
        });

        customerPulls.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { pullListSelectionChanged(); }
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

        nameButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                SelectionChanged();
            }
        });

        diamondButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                SelectionChanged();
            }
        });

        issueButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                SelectionChanged();
            }
        });

        nonBookButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                SelectionChanged();
            }
        });

        graphicButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                SelectionChanged();
            }
        });
        SetMatchesList(Data.DB().getSearchTermsNames());
        //nameFill();
        addToPullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToPull();
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

    private void addToPull() {
        String cuid = Data.DB().getCustomerID(name, phone, email);
        boolean selected = !inInventory.isSelectionEmpty();
        if (!selected) return;
        String searchID = Data.DB().getSearchTermId(Data.Store(), inInventory.getSelectedValue().toString());
        Data.DB().insertPullList(Data.Store(), cuid, searchID, "1");
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

    private void SetPullsList(Vector<String> comic) {
        customerPulls.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String c: comic) data.addElement(c);
        customerPulls.setModel(data);
    }

    /**
     * Dispose of the JFrame.
     */
    public static void Dispose() { // dispose sub windows
        frame.dispose();
    }

    /**
     * Get and set the customer list.
     */
    private void nameFill() {
        inInventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        possibleMatches = Data.DB().getSearchTermsNames();
        SetMatchesList(possibleMatches);
        frame.setVisible(true);
    }

    private void diamondFill() {
        inInventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        possibleMatches = Data.DB().getSearchTermsDiamond();
        SetMatchesList(possibleMatches);
        frame.setVisible(true);
    }

    private void nonBookFill() {
        inInventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        possibleMatches = Data.DB().getSearchTermsNonBook();
        SetMatchesList(possibleMatches);
        frame.setVisible(true);
    }

    private void graphicNovelFill() {
        inInventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        possibleMatches = Data.DB().getSearchTermsGraphic();
        SetMatchesList(possibleMatches);
        frame.setVisible(true);
    }

    private void issueNumberFill() {
        inInventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        possibleMatches = Data.DB().getSearchTermsIssue();
        SetMatchesList(possibleMatches);
        frame.setVisible(true);
    }

    private void chooseItems() {
        if (nameButton.isSelected()) {
            nameFill();
        }
        else if (diamondButton.isSelected()) {
            diamondFill();
        }
        else if (nonBookButton.isSelected()) {
            nonBookFill();
        }
        else if (graphicButton.isSelected()) {
            graphicNovelFill();
        }
        else if (issueButton.isSelected()) {
            issueNumberFill();
        }
    }

    private void createUIComponents() {
        // TODOo: place custom component creation code here
        nameButton = new JRadioButton("Name");
    }

    /**
     * Set the context.
     */
    private void pullListSelectionChanged() {
        chooseItems();

    }

    private void SelectionChanged() {
        customerPulls.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customerRequests = Data.DB().getDiamondCode();
        SetPullsList(customerRequests);
        frame.setVisible(true);
    }
}
