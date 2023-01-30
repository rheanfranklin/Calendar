package main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class DialogEventEdit extends JDialog {
    private final EventModel event;
    private EventModel origEvent;
    private final CalModel calModel;
    private final DialogEventEdit dialogEventEdit;

    public DialogEventEdit(CalModel calModel, EventModel origEvent) {
        event = new EventModel(origEvent);
        this.origEvent = origEvent;
        this.calModel = calModel;
        this.dialogEventEdit = this;
        setUp();
    }


    public void setUp() {
        final DialogEventEditHelper editHelper = new DialogEventEditHelper();

        setSize(600, 350);
        setLocationRelativeTo(calModel.getDayView());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Event Edit");
        setBackground(CalModel.BACKGROUND_COLOR);

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(CalModel.BACKGROUND_COLOR);
        getContentPane().add(backgroundPanel);

        //setting up border spacing
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(10,1, 1, 2));
        dialogPanel.setBackground(CalModel.BACKGROUND_COLOR);
        backgroundPanel.add(BorderLayout.CENTER, dialogPanel);

        JLabel spacing1 = new JLabel("      ");
        JLabel spacing2 = new JLabel("      ");
        JLabel spacing3 = new JLabel("   ");
        JLabel spacing4 = new JLabel("   ");

        backgroundPanel.add(BorderLayout.WEST, spacing1);
        backgroundPanel.add(BorderLayout.EAST, spacing2);
        backgroundPanel.add(BorderLayout.SOUTH, spacing3);
        backgroundPanel.add(BorderLayout.NORTH, spacing4);

        //setting up title
        JTextField titleField = new JTextField(event.getTitle().equals("(No title)") ? "Add title" : event.getTitle());
        Font font = new Font("Courtier", Font.BOLD, 15);
        titleField.setFont(font);
        titleField.setForeground(Color.WHITE);
        titleField.setBackground(CalModel.BACKGROUND_COLOR);
        titleField.setBorder(new LineBorder(CalModel.BACKGROUND_COLOR, 2));
        dialogPanel.add(titleField);

        //setting up start time and date fields
        JLabel startDatePrompt = new JLabel("Start: ");
        startDatePrompt.setBackground(CalModel.BACKGROUND_COLOR);
        startDatePrompt.setForeground(Color.WHITE);
        dialogPanel.add(startDatePrompt);

        JPanel holdStartDateField = new JPanel(new GridLayout(1, 2, 50, 1));
        holdStartDateField.setBackground(CalModel.BACKGROUND_COLOR);
        dialogPanel.add(holdStartDateField);

        JPanel holdStartDate = new JPanel(new GridLayout(1, 3, 1, 1));
        holdStartDate.setBackground(CalModel.BACKGROUND_COLOR);
        holdStartDateField.add(holdStartDate);

        Integer[] daysOfMonth = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
                , 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };

        JComboBox<Integer> startDayList = new JComboBox<>(daysOfMonth);
        startDayList.setSelectedIndex(event.getEventStartDate().getDayOfMonth() - 1);
        startDayList.setMaximumSize(new Dimension(5, 5));

        holdStartDate.add(startDayList);

        String[] months = { "January" , "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December" };

        JComboBox<String> startMonthList = new JComboBox<>(months);
        startMonthList.setSelectedIndex(event.getEventStartDate().getMonthValue() - 1);
        holdStartDate.add(startMonthList);

        Integer[] years = {2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030};

        JComboBox<Integer> startYearList = new JComboBox<>(years);
        startYearList.setSelectedIndex(event.getEventStartDate().getYear() - 2021);
        holdStartDate.add(startYearList);


        JPanel holdStartTime = new JPanel(new GridLayout(1, 3, 1, 1));
        holdStartTime.setBackground(CalModel.BACKGROUND_COLOR);
        holdStartDateField.add(holdStartTime);

        String[] hours = {"01", "02", "03", "04", "05", "06", "07", "08","09", "10", "11", "12"};

        JComboBox<String> startHourList = new JComboBox<>(hours);
        int eventStartHour = event.getEventStartTime().getHour() % 12;
        startHourList.setSelectedIndex(eventStartHour == 0 ? 11 : eventStartHour - 1);
        holdStartTime.add(startHourList);

        String[] minutes = {"00", "05", "10", "15", "20", "25", "30","35", "40", "45" , "50", "55"};
        JComboBox<String> startMinList = new JComboBox<>(minutes);
        startMinList.setSelectedIndex(event.getEventStartTime().getMinute() / 5);
        holdStartTime.add(startMinList);

        String[] amPM = {"AM", "PM"};
        JComboBox<String> startAMOrPM = new JComboBox<>(amPM);
        holdStartTime.add(startAMOrPM);
        if (event.getEventStartTime().getHour() > 11) {
            startAMOrPM.setSelectedIndex(1);
        }


        dialogPanel.add(holdStartDateField);



        //Setting up end date and time fields
        JLabel endDatePrompt = new JLabel("End: ");
        endDatePrompt.setBackground(CalModel.BACKGROUND_COLOR);
        endDatePrompt.setForeground(Color.WHITE);
        dialogPanel.add(endDatePrompt);

        JPanel holdEndDateField = new JPanel(new GridLayout(1, 8, 50, 1));
        holdEndDateField.setBackground(CalModel.BACKGROUND_COLOR);

        JPanel holdEndDate = new JPanel(new GridLayout(1, 3, 1, 1));
        holdEndDate.setBackground(CalModel.BACKGROUND_COLOR);
        holdEndDateField.add(holdEndDate);

        JComboBox<Integer> endDayList = new JComboBox<>(daysOfMonth);
        endDayList.setSelectedIndex(event.getEventEndDate().getDayOfMonth() - 1);
        endDayList.setMaximumSize(new Dimension(5, 5));

        holdEndDate.add(endDayList);

        JComboBox<String> endMonthList = new JComboBox<>(months);
        endMonthList.setSelectedIndex(event.getEventEndDate().getMonthValue() - 1);
        holdEndDate.add(endMonthList);

        JComboBox<Integer> endYearList = new JComboBox<>(years);
        endYearList.setSelectedIndex(event.getEventEndDate().getYear() - 2021);
        holdEndDate.add(endYearList);

        JPanel holdEndTime = new JPanel(new GridLayout(1, 3, 1, 1));
        holdEndTime.setBackground(CalModel.BACKGROUND_COLOR);
        holdEndDateField.add(holdEndTime);

        JComboBox<String> endHourList = new JComboBox<>(hours);
        int selectHour = event.getEventEndTime().getHour();
        if (selectHour > 12) {
            selectHour -= 12;
        } else if (selectHour == 0) {
            selectHour = 12;
        }
        endHourList.setSelectedIndex(selectHour - 1);
        holdEndTime.add(endHourList);

        JComboBox<String> endMinList = new JComboBox<>(minutes);
        endMinList.setSelectedIndex(event.getEventEndTime().getMinute() / 5);
        holdEndTime.add(endMinList);

        JComboBox<String> endAMOrPM = new JComboBox<>(amPM);
        holdEndTime.add(endAMOrPM);
        if (event.getEventEndTime().getHour() > 11) {
            endAMOrPM.setSelectedIndex(1);
        }

        dialogPanel.add(holdEndDateField);

        //setting up location field
        JTextField locationField = new JTextField(event.getLocation().equals("") ? "Add location" : event.getLocation());
        locationField.setForeground(CalModel.FONT_COLOR);
        locationField.setBackground(CalModel.BACKGROUND_COLOR);
        locationField.setBorder(new LineBorder(CalModel.BACKGROUND_COLOR, 2));
        dialogPanel.add(locationField);

        //setting up description field
        JTextField descriptionField = new JTextField(event.getDescription().equals("") ? "Add description" : event.getDescription());
        descriptionField.setForeground(CalModel.FONT_COLOR);
        descriptionField.setBackground(CalModel.BACKGROUND_COLOR);
        descriptionField.setBorder(new LineBorder(CalModel.BACKGROUND_COLOR, 2));
        dialogPanel.add(descriptionField);


        //setting up tags
        JCheckBox cat1 = new JCheckBox("Vacation");
        JCheckBox cat2 = new JCheckBox("School");
        JCheckBox cat3 = new JCheckBox("Work");
        JCheckBox cat4 = new JCheckBox("Family");

        if (event.getTagStatus(EventModel.Tag.VACATION)) {
            cat1.setSelected(true);
        }

        if (event.getTagStatus(EventModel.Tag.SCHOOL)) {
            cat2.setSelected(true);
        }

        if (event.getTagStatus(EventModel.Tag.WORK)) {
            cat3.setSelected(true);
        }

        if (event.getTagStatus(EventModel.Tag.FAMILY)) {
            cat4.setSelected(true);
        }


        cat1.setBackground(CalModel.BACKGROUND_COLOR);
        cat2.setBackground(CalModel.BACKGROUND_COLOR);
        cat3.setBackground(CalModel.BACKGROUND_COLOR);
        cat4.setBackground(CalModel.BACKGROUND_COLOR);

        cat1.setForeground(Color.WHITE);
        cat2.setForeground(Color.WHITE);
        cat3.setForeground(Color.WHITE);
        cat4.setForeground(Color.WHITE);

        JPanel holdCatBoxes = new JPanel();
        holdCatBoxes.setBackground(CalModel.BACKGROUND_COLOR);
        holdCatBoxes.setLayout(new GridLayout(1, 4, 1, 1));

        holdCatBoxes.add(cat1);
        holdCatBoxes.add(cat2);
        holdCatBoxes.add(cat3);
        holdCatBoxes.add(cat4);

        dialogPanel.add(holdCatBoxes);


        JPanel holdButtons = new JPanel();
        holdButtons.setLayout(new GridLayout(1, 3));

        JPanel holdColors = new JPanel();
        holdColors.setBackground(CalModel.BACKGROUND_COLOR);
        JLabel colorPrompt = new JLabel("Color: ");
        colorPrompt.setForeground(Color.WHITE);
        String[] colors = {"Blue", "Purple", "Red", "Yellow", "Green", "Orange"};

        JComboBox<String> colorList = new JComboBox<>(colors);
        colorList.setSelectedIndex(0);
        holdColors.add(colorPrompt);
        holdColors.add(colorList);
        dialogPanel.add(holdColors);


        Font buttonFont = new Font("Courtier", Font.BOLD, 12);

        JButton save = new JButton("Save");
        save.setBackground(CalModel.BACKGROUND_COLOR);
        save.setPreferredSize(new Dimension(25, 10));
        save.setBackground(CalModel.BACKGROUND_COLOR);
        save.setForeground(Color.WHITE);
        save.setFont(buttonFont);
        save.setOpaque(true);
        save.setBorderPainted(false);
        holdButtons.add(save);

        holdButtons.add(Box.createRigidArea(new Dimension(3,0)));

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(CalModel.BACKGROUND_COLOR);
        cancel.setForeground(Color.RED);
        cancel.setFont(buttonFont);
        cancel.setOpaque(true);
        cancel.setBorderPainted(false);
        holdButtons.add(cancel);
        holdButtons.setBackground(CalModel.BACKGROUND_COLOR);

        dialogPanel.add(holdButtons);

        event.setTitle(titleField.getText().equals("Add title") ? "(No title)" : titleField.getText());
        titleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                event.setTitle(titleField.getText().equals("Add title") ? "(No title)" : titleField.getText());
            }
        });

        titleField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                event.setTitle(titleField.getText().equals("Add title") ? "(No title)" : titleField.getText());
            }
        });

        editHelper.setStartDay(startDayList.getSelectedIndex() + 1);
        startDayList.addActionListener(e -> editHelper.setStartDay(startDayList.getSelectedIndex() + 1));

        editHelper.setStartMonth(startMonthList.getSelectedIndex() + 1);
        startMonthList.addActionListener(e -> editHelper.setStartMonth(startMonthList.getSelectedIndex() + 1));

        editHelper.setStartYear(startYearList.getSelectedIndex() + 2021);
        startYearList.addActionListener(e -> editHelper.setStartYear(startYearList.getSelectedIndex() + 2021));

        editHelper.setStartHour(startHourList.getSelectedIndex() + 1);
        startHourList.addActionListener(e -> editHelper.setStartHour(startHourList.getSelectedIndex() + 1));

        editHelper.setStartMinute(startMinList.getSelectedIndex() * 5);
        startMinList.addActionListener(e -> editHelper.setStartMinute(startMinList.getSelectedIndex() * 5));

        editHelper.setIsStartAM(startAMOrPM.getSelectedIndex() == 0);
        startAMOrPM.addActionListener(e -> editHelper.setIsStartAM(startAMOrPM.getSelectedIndex() == 0));

        editHelper.setEndDay(endDayList.getSelectedIndex() + 1);
        endDayList.addActionListener(e -> editHelper.setEndDay(endDayList.getSelectedIndex() + 1));

        editHelper.setEndMonth(endMonthList.getSelectedIndex() + 1);
        endMonthList.addActionListener((e -> editHelper.setEndMonth(endMonthList.getSelectedIndex() + 1)));

        editHelper.setEndYear(endYearList.getSelectedIndex() + 2021);
        endYearList.addActionListener((e -> editHelper.setEndYear(endYearList.getSelectedIndex())));

        editHelper.setEndHour(endHourList.getSelectedIndex() + 1);
        endHourList.addActionListener(e -> editHelper.setEndHour(endHourList.getSelectedIndex() + 1));

        editHelper.setEndMinute(endMinList.getSelectedIndex() * 5);
        endMinList.addActionListener(e -> editHelper.setEndMinute(endMinList.getSelectedIndex() * 5));

        editHelper.setEndAm(endAMOrPM.getSelectedIndex() == 0);
        endAMOrPM.addActionListener(e -> editHelper.setEndAm(endAMOrPM.getSelectedIndex() == 0));

        event.setLocation(locationField.getText().equals("Add location") ? "" : locationField.getText());
        locationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                event.setLocation(locationField.getText().equals("Add location") ? "" : locationField.getText());
            }
        });

        locationField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                event.setLocation(locationField.getText().equals("Add location") ? "" : locationField.getText());
            }
        });

        event.setDescription(descriptionField.getText().equals("Add description") ? "" : descriptionField.getText());
        descriptionField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                event.setDescription(descriptionField.getText().equals("Add description") ? "" : descriptionField.getText());
            }
        });

        descriptionField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                event.setDescription(descriptionField.getText().equals("Add description") ? "" : descriptionField.getText());
            }
        });

        cat1.addItemListener(e -> event.setTag(EventModel.Tag.VACATION, e.getStateChange() == ItemEvent.SELECTED));

        cat2.addItemListener(e -> event.setTag(EventModel.Tag.SCHOOL, e.getStateChange() == ItemEvent.SELECTED));

        cat3.addItemListener(e -> event.setTag(EventModel.Tag.WORK, e.getStateChange() == ItemEvent.SELECTED));

        cat4.addItemListener(e -> event.setTag(EventModel.Tag.FAMILY, e.getStateChange() == ItemEvent.SELECTED));

        editHelper.setColor(colorList.getSelectedIndex(), event);
        colorList.addActionListener(e -> {
            editHelper.setColor(colorList.getSelectedIndex(), event);
        });

        save.addActionListener(e -> {
            event.setEventStart(editHelper.returnStartTime());
            event.setEventEnd(editHelper.returnEndTime());
            event.setTitle(titleField.getText().equals("Add title") ? "(No title)" : titleField.getText());
            event.setLocation(locationField.getText().equals("Add location") ? "" : locationField.getText());
            event.setDescription(descriptionField.getText().equals("Add description") ? "" : descriptionField.getText());
            if (calModel.containsEvent(origEvent)) {
                calModel.removeEvent(origEvent);
            }
            event.setTag(EventModel.Tag.VACATION, cat1.isSelected());
            event.setTag(EventModel.Tag.SCHOOL, cat2.isSelected());
            event.setTag(EventModel.Tag.WORK, cat3.isSelected());
            event.setTag(EventModel.Tag.FAMILY, cat4.isSelected());
            event.setEventDayView(origEvent.getEventDayView());
            calModel.addEvent(event);
            calModel.closeAllDialogs();
            dispose();
        });

        cancel.addActionListener(e -> calModel.removedOpenDialog(dialogEventEdit));
    }

}
