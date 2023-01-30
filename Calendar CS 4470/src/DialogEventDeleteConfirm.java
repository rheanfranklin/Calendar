package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogEventDeleteConfirm extends JDialog {

    public DialogEventDeleteConfirm(CalModel calModel) {

        setSize(200, 100);
        setLocationRelativeTo(calModel.getDayView());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Event Deleted ");
        setBackground(CalModel.BACKGROUND_COLOR);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(2, 1));
        getContentPane().add(dialogPanel);
        dialogPanel.setBackground(CalModel.BACKGROUND_COLOR);


        JLabel deleteConfirm = new JLabel("Your event has been deleted");
        deleteConfirm.setForeground(Color.WHITE);
        dialogPanel.add(deleteConfirm);
        deleteConfirm.setHorizontalAlignment(JLabel.CENTER);

        JButton ok = new JButton("OK");

        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new GridLayout(1, 2));
        buttonHolder.add(Box.createRigidArea(new Dimension(3, 0)));
        buttonHolder.add(ok);
        buttonHolder.setBackground(CalModel.BACKGROUND_COLOR);

        dialogPanel.add(buttonHolder);


        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calModel.closeAllDialogs();
            }
        });
    }

}
