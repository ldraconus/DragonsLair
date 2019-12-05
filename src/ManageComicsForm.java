import javafx.scene.control.TableSelectionModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ManageComicsForm {
    private JPanel manageComicsPanel;
    private JTextField searchTextField;
    private JButton deleteButton;
    private JButton addButton;
    private JButton editButton;
    private JButton doneButton;
    private JTable comicTable;

    private static JFrame frame = null;

    DefaultTableModel defaultModel;
    private Vector<Comic> comics;

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
                AddComic();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditComic();
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
                //searchComics();
            }
        });

        comicTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
        String[] columns = {"Comic name","Diamond Code"};
        defaultModel = new DefaultTableModel(columns, 0);
        comicTable.setModel(defaultModel);

        // Retrieve comics from the database and add them to the table model
        comics = Data.DB().getCSVEntries();
        for(Comic c : comics){
            Object[] rowData = {c.getTitle(), c.getDiamondCode()};
            defaultModel.addRow(rowData);
        }

        comicTable.setVisible(true);
        SetContext();
        frame.setVisible(true);
    }

    /**
     * Enable or disable add, delete, and edit.
     */
    private void SetContext() {
        addButton.setEnabled(true);
        deleteButton.setEnabled(!comicTable.getSelectionModel().isSelectionEmpty());
        editButton.setEnabled(!comicTable.getSelectionModel().isSelectionEmpty());
    }

    /**
     *  [CHANGES NEED] : Search filter
     */
    /*
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
    }*/

    /**
     * Delete a comic to the database and JTable.
     */
    private void Delete() {
        if(comicTable.getSelectionModel().isSelectionEmpty())
            return;
        
        int selectedRow = comicTable.getSelectedRow();
        String comicTitle = comicTable.getModel().getValueAt(selectedRow, 0).toString();
        String diamondCode = comicTable.getModel().getValueAt(selectedRow, 1).toString();
        Message msg = new Message(Message.YesNoMessage, "Are you sure you want to delete " + comicTitle + "?");
        if (msg.getButton() == Message.NoButton) return;
        Data.DB().deleteCsvEntry(Data.Store(), diamondCode);

        // Update JTable.
        ((DefaultTableModel)comicTable.getModel()).removeRow(selectedRow);
    }

    /**
     * Add a comic to the database and JTable.
     */
    private void AddComic(){
        new AddOrEditComic("", "", "", "", "", "");
        if(AddOrEditComic.Finished()){
            System.out.println(AddOrEditComic.ComicTitle());

            Data.DB().insertCsvEntries(AddOrEditComic.ComicTitle(), AddOrEditComic.DiamondCode(), AddOrEditComic.Issue(),
                    AddOrEditComic.GraphicNovel(), AddOrEditComic.NonBook(), AddOrEditComic.CsvId(), Data.Store());

            // Update JTable.
            Object[] rowData = {AddOrEditComic.ComicTitle(), AddOrEditComic.DiamondCode()};
            ((DefaultTableModel)comicTable.getModel()).addRow(rowData);
        }
    }

    /**
     * Edit a comic.
     */
    private void EditComic(){
        if(comicTable.getSelectionModel().isSelectionEmpty())
            return;

        int selectedRow = comicTable.getSelectedRow();
        String diamondCode = comicTable.getModel().getValueAt(selectedRow, 1).toString();
        Comic comic = Data.DB().getCsvEntry(Data.Store(), diamondCode);

        new AddOrEditComic(comic.getTitle(), comic.getDiamondCode(), comic.getGraphicNovel(), comic.getNonBook(), comic.getIssue(), comic.getCsvDate());
        if(AddOrEditComic.Finished()) {
            Comic newComic = new Comic(AddOrEditComic.ComicTitle(), AddOrEditComic.DiamondCode());
            newComic.setIssue(AddOrEditComic.Issue());
            newComic.setGraphicNovel(AddOrEditComic.GraphicNovel());
            newComic.setNonBook(AddOrEditComic.NonBook());
            Data.DB().updateCsvEntry(Data.Store(), newComic, AddOrEditComic.CsvId(), comic.getDiamondCode());
            Open();
        }
    }

    private void Done() {
        Dispose();
    }
}
