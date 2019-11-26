import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Vector;

public class ManageComicsForm {
    private JPanel manageComicsPanel;
    private JTextField searchTextField;
    private JButton deleteButton;
    private JButton addButton;
    private JButton editButton;
    private JList<String> comicList;
    private JButton doneButton;

    private static JFrame frame = null;

    private Vector<String> comics;

    public static void Dispose() { // dispose sub windows
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Manage Comics");
            frame.setContentPane(new ManageComicsForm().manageComicsPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    public ManageComicsForm() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) { Open(); }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Delete();
            }
        });
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        });
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                searchComics();
            }
        });
        comicList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SetContext();
            }
        });

        manageComicsPanel.registerKeyboardAction(new ActionListener() {
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
    }

    /**
     * Get and set the customer list.
     */
    private void Open() {
        comicList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        comics = Data.DB().getCsvEntries();
        SetComicList(comics);
        SetContext();
        frame.setVisible(true);
    }

    /**
     * Set the comic list.
     * @param comics Used for filtering.
     */
    private void SetComicList(Vector<String> comics) {
        comicList.clearSelection();
        DefaultListModel<String> data = new DefaultListModel<String>();
        for (String comic: comics) data.addElement(comic);
        comicList.setModel(data);
    }

    /**
     * Enable or disable add, delete, and edit.
     */
    private void SetContext() {
        addButton.setEnabled(true);
        deleteButton.setEnabled(!comicList.isSelectionEmpty());
        editButton.setEnabled(!comicList.isSelectionEmpty());
    }

    /**
     * Search filter
     */
    private void searchComics() {
        String text = searchTextField.getText();
        if (text.isEmpty()) {
            SetComicList(comics);
            SetContext();
            return;
        }
        Vector<String> filtered = new Vector();
        for (String comic: comics) if (comic.toLowerCase().contains(text.toLowerCase())) filtered.addElement(comic);
        SetComicList(filtered);
        SetContext();
    }

    /**
     * Delete customer.
     */
    private void Delete() {
        boolean selected = !comicList.isSelectionEmpty();
        if (!selected) return;
        String selectedComic = comicList.getSelectedValue();
        Message msg = new Message(Message.YesNoMessage, "Are you sure you want to delete " + selectedComic + "?");
        if (msg.getButton() == Message.NoButton) return;
        Data.DB().deleteCsvEntry(Data.Store(), selectedComic);
        Open();
    }

    private void Done() {
        frame.setVisible(false);
    }
}
