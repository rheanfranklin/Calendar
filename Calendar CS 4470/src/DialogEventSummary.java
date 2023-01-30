package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogEventSummary extends JDialog {
    private EventModel eventModel;
    private CalModel calModel;
    private DialogEventSummary dialogEventSummary;


    public DialogEventSummary(CalModel calModel,EventModel eventModel) {
        this.eventModel = eventModel;
        this.calModel = calModel;
        dialogEventSummary = this;
        setUp();
    }

    private void setUp() {
        setSize(400, 200);
        setLocationRelativeTo(calModel.getDayView());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(eventModel.getTitle());
        setBackground(CalModel.BACKGROUND_COLOR);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(6,1));
        getContentPane().add(dialogPanel);
        dialogPanel.setBackground(CalModel.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("    " + eventModel.getTitle());
        Font font = new Font("Courtier", Font.BOLD, 20);
        titleLabel.setFont(font);
        titleLabel.setForeground(Color.WHITE);
        dialogPanel.add(titleLabel);

        int hour = eventModel.getEventStartTime().getHour();
        String minute = "" + (eventModel.getEventStartTime().getMinute() < 10 ? "0" + eventModel.getEventStartTime().getMinute()
                : eventModel.getEventStartTime().getMinute());
        String startTime = (hour > 12 ? (hour % 12) + ":" + minute + " PM" : hour + ":" + minute + " AM");

        hour = eventModel.getEventEndTime().getHour();
        minute = "" + (eventModel.getEventEndTime().getMinute() < 10 ? "0" + eventModel.getEventEndTime().getMinute()
                : eventModel.getEventEndTime().getMinute());
        String endTime = (hour > 12 ? (hour % 12) + ":" + minute + " PM" : hour + ":" + minute + " AM");

        String dayOfWeek = "       " + eventModel.getEventStartDate().getDayOfWeek();

        String time = dayOfWeek + " | " + startTime + " - " + endTime;

        JLabel timeLabel = new JLabel(time);
        timeLabel.setForeground(CalModel.FONT_COLOR);
        dialogPanel.add(timeLabel);

        JLabel locationLabel = new JLabel("       " + eventModel.getLocation());
        locationLabel.setForeground(CalModel.FONT_COLOR);
        dialogPanel.add(locationLabel);

        JLabel descriptionLabel = new JLabel("       " + eventModel.getDescription());
        descriptionLabel.setForeground(CalModel.FONT_COLOR);
        dialogPanel.add(descriptionLabel);

        JLabel spacing = new JLabel("     ");
        dialogPanel.add(spacing);

        JPanel holdButtonHolder = new JPanel();
        holdButtonHolder.setLayout(new GridLayout(1, 3));

        JPanel holdButtons = new JPanel();
        holdButtons.setLayout(new GridLayout(1, 2));

        holdButtonHolder.add(holdButtons);
        holdButtonHolder.add(Box.createRigidArea(new Dimension(3,0)));

        JButton edit = new JButton("Edit");
        edit.setBackground(CalModel.BACKGROUND_COLOR);
        edit.setPreferredSize(new Dimension(25, 10));
        holdButtons.add(edit);
        dialogPanel.add(holdButtonHolder);

        JButton cancel = new JButton("Cancel");
        holdButtons.add(cancel);
        holdButtons.setBackground(CalModel.BACKGROUND_COLOR);

        JButton delete = new JButton("Delete");
        holdButtonHolder.add(delete);
        holdButtonHolder.setBackground(CalModel.BACKGROUND_COLOR);

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogEventEdit dialogEventEdit = new DialogEventEdit(calModel, eventModel);
                calModel.addOpenDialog(dialogEventEdit);
                dialogEventEdit.setVisible(true);
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calModel.removedOpenDialog(dialogEventSummary);
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogEventDelete dialogEventDeleteConfirm = new DialogEventDelete(calModel, eventModel);
                calModel.addOpenDialog(dialogEventDeleteConfirm);
            }
        });

    }

    public EventModel getEvent() { return eventModel; }

}
