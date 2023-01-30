package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogEventDelete extends JDialog {
    private CalModel calModel;
    private EventModel eventModel;
    private DialogEventDelete dialogEventDelete;

    public DialogEventDelete(CalModel calModel, EventModel eventModel) {
        this.calModel = calModel;
        this.eventModel = eventModel;
        dialogEventDelete = this;
        setUp();
    }

    private void setUp() {

        setSize(350, 100);
        setLocationRelativeTo(calModel.getDayView());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Delete: " + eventModel.getTitle());
        setBackground(CalModel.BACKGROUND_COLOR);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(2, 1));
        getContentPane().add(dialogPanel);
        dialogPanel.setBackground(CalModel.BACKGROUND_COLOR);

        JLabel deletePrompt = new JLabel("Are you sure you want to delete " + eventModel.getTitle() + "?");
        deletePrompt.setHorizontalAlignment(JLabel.CENTER);
        Font font = new Font("Courtier", Font.PLAIN, 12);
        deletePrompt.setFont(font);
        deletePrompt.setForeground(Color.WHITE);
        dialogPanel.add(deletePrompt);

        JPanel holdButtons = new JPanel();
        holdButtons.setLayout(new GridLayout(1, 2));

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        holdButtons.add(Box.createRigidArea(new Dimension(3, 0)));
        holdButtons.add(no);
        holdButtons.add(Box.createRigidArea(new Dimension(3, 0)));
        holdButtons.add(yes);
        holdButtons.add(Box.createRigidArea(new Dimension(3, 0)));
        holdButtons.setBackground(CalModel.BACKGROUND_COLOR);

        dialogPanel.add(holdButtons);

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calModel.removeEvent(eventModel);
                DialogEventDeleteConfirm dialogEventDeleteConfirm = new DialogEventDeleteConfirm(calModel);
                calModel.addOpenDialog(dialogEventDeleteConfirm);
                dialogEventDeleteConfirm.setVisible(true);
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calModel.removedOpenDialog(dialogEventDelete);

            }
        });
    }
}
