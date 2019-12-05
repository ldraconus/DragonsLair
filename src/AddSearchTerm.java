import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class AddSearchTerm extends JFrame{
    private Vector<String> possiblePulls;
    private JTextField diamondCodeField;
    private JTextField issueField;
    private JTextField displayNameField;
    private JCheckBox graphicCheckBox;
    private JCheckBox nonBookCheckBox;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private JList inInventory;
    private JTextField searchField;

    private static JFrame frame = null;

    public AddSearchTerm() {

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchNames();
            }
        });

        SetMatchesList(Data.DB().getCsvEntries());
    }

    private void onCancel() {
        if (frame != null) { frame.dispose(); }
    }

    /**
     * Create a new JFrame and add the initial UI components.
     */
    public void Display() {
        if (frame == null) {
            frame = new JFrame("Add Pull Terms");
            frame.setContentPane(contentPane);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    private void onOk() {
        String displayName = displayNameField.getText();
        String diamondName = diamondCodeField.getText();
        String issueName = issueField.getText();
        String inventorySelection = null;
        if (!inInventory.isSelectionEmpty()) {
            inventorySelection = inInventory.getSelectedValue().toString();
        }
        String graphicNovel = graphicCheckBox.isSelected() ? "1" : "0";
        String nonBook = nonBookCheckBox.isSelected() ? "1" : "0";


        if (displayName.compareTo("") == 0) {
            String message = "Please enter a display name";
            JOptionPane.showMessageDialog(null, message);
            return;
        }
        else if (inventorySelection == null) {
            String message = "Please select an item for this name to match to.";
            JOptionPane.showMessageDialog(null, message);
            return;
        }

        if (diamondName.isEmpty()) {
            diamondName = null;
        }

        if (issueName.isEmpty()) {
            issueName = null;
        }

        Data.DB().insertSearchTerms(Data.Store(), displayName, diamondName, issueName,graphicNovel, nonBook, inventorySelection);
        updatePossiblePulls();
        onCancel();
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

    private void updatePossiblePulls() {
        setPossiblePulls();
        SetMatchesList(possiblePulls);
    }

    private void searchNames() {
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
}
