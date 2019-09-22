import javax.swing.*;
import java.awt.event.*;

public class Message extends JDialog {
    private JPanel contentPane;
    private JLabel Message;
    private JButton buttonYesOk;
    private JButton buttonOkNo;
    private JButton buttonNoCancel;

    public static final int Nothing = -1;

    public static final int YesButton = 0;
    public static final int NoButton = 1;
    public static final int CancelButton = 2;
    public static final int OkButton = 3;

    public static final int OKMessage = 0;
    public static final int YesNoMessage = 1;
    public static final int OKCancelMessage = 2;
    public static final int YesNoCancelMessage = 3;

    int dialogType = Nothing;
    static int button = Nothing;

    public int getButton() {
        return button;
    }

    public Message(int type, String msg) {
        setContentPane(contentPane);

        setModal(true);
        Message.setText(msg);

        dialogType = type;
        switch (type) {
            case OKMessage:
                buttonYesOk.setVisible(false);
                buttonOkNo.setVisible(true);
                buttonNoCancel.setVisible(false);
                buttonOkNo.setText("OK");
                getRootPane().setDefaultButton(buttonOkNo);
                break;
            case YesNoMessage:
                buttonYesOk.setVisible(true);
                buttonOkNo.setVisible(false);
                buttonNoCancel.setVisible(true);
                buttonYesOk.setText("Yes");
                buttonNoCancel.setText("No");
                getRootPane().setDefaultButton(buttonYesOk);
                break;
            case OKCancelMessage:
                buttonYesOk.setVisible(true);
                buttonOkNo.setVisible(false);
                buttonNoCancel.setVisible(true);
                buttonYesOk.setText("OK");
                buttonNoCancel.setText("Cancel");
                getRootPane().setDefaultButton(buttonYesOk);
                break;
            case YesNoCancelMessage:
                buttonYesOk.setVisible(true);
                buttonOkNo.setVisible(true);
                buttonNoCancel.setVisible(true);
                buttonYesOk.setText("Yes");
                buttonOkNo.setText("No");
                buttonNoCancel.setText("Cancel");
                getRootPane().setDefaultButton(buttonYesOk);
                break;
            default:
                dialogType = Nothing;
                break;
        }

        buttonYesOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButton1();
            }
        });

        buttonOkNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButton2();
            }
        });

        buttonNoCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButton3();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                switch (dialogType) {
                    case OKMessage:
                        onButton2();
                        break;
                    case YesNoMessage:
                    case YesNoCancelMessage:
                    case OKCancelMessage:
                        onButton3();
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (dialogType) {
                    case OKMessage:
                        onButton2();
                        break;
                    case YesNoMessage:
                    case YesNoCancelMessage:
                    case OKCancelMessage:
                        onButton3();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.pack();
        this.setVisible(true);
    }

    private void onButton1() {
        switch (dialogType) {
            case OKMessage:          button = Nothing;   break;
            case YesNoMessage:       button = YesButton; break;
            case OKCancelMessage:    button = OkButton;  break;
            case YesNoCancelMessage: button = YesButton; break;
        }
        // add your code here
        dispose();
    }

    private void onButton2() {
        switch (dialogType) {
            case OKMessage:          button = OkButton;     break;
            case YesNoMessage:       button = Nothing;      break;
            case OKCancelMessage:    button = Nothing;      break;
            case YesNoCancelMessage: button = CancelButton; break;
        }
        // add your code here if necessary
        dispose();
    }

    private void onButton3() {
        switch (dialogType) {
            case OKMessage:          button = Nothing;      break;
            case YesNoMessage:       button = NoButton;     break;
            case OKCancelMessage:    button = CancelButton; break;
            case YesNoCancelMessage: button = CancelButton; break;
        }
        // add your code here if necessary
        dispose();
    }
}
