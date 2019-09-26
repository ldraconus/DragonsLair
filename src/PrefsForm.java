import com.mysql.cj.x.protobuf.MysqlxResultset;

import javax.swing.*;
import java.awt.event.*;

public class PrefsForm {
    private JButton manageStoresButton;
    private JPanel PrefsPanel;
    private JButton manageLoginsButton;
    private JButton doneButton;

    private static JFrame frame;

    public static JFrame Frame() { return frame; }
    public static void Dispose() { // dispose sub window
        ManageUsersForm.Dispose();
        ManageStoresForm.Dispose();
        if (frame != null) frame.dispose();
    }

    public static void Display() {
        if (frame == null) {
            frame = new JFrame("Dragon's Lair Preferences");
            frame.setContentPane(new PrefsForm().PrefsPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    public PrefsForm() {
        PrefsPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Done();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) { Done(); }
        });

        manageStoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { ManageStores(); }
        });
        manageLoginsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { ManageLogins(); }
        });
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { Done(); }
        });
    }

    private void ManageLogins() {
        ManageUsersForm.Display();
    }

    private void ManageStores() { ManageStoresForm.Display(); }

    private void Done() {
        frame.setVisible(false);
    }
}
