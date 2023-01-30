package main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that holds all the data that a CalView needs to display events
 *
 * Add and remove events here
 * 
 * Add and remove dialogs here
 *
 * Extends CalModel
 */

public class CalModel {
    //static final variables
    public static final Color BACKGROUND_COLOR = new Color(50,50, 50);
    public static final Color FONT_COLOR = new Color(150, 150, 150);


    //final variables
    /**
     * All the dialogs that are open
     */
    private final ArrayList<JDialog> openDialogs;
    
    /**
     * A hashmap that holds all the events per date
     */
    private final HashMap<LocalDate, ArrayList<EventModel>> eventList;

    /**
     * The variable that paints the day view component
     */
    private final DayView dayView;

    /**
     * The variable that paints the month view component
     */
    private final MonthView monthView;

    /**
     * The bottom label displayed on the CalView that changes based on user interaction
     */
    private final JLabel bottomLabel;


    //regular variables
    /**
     * Current date of the calendar
     */
    private LocalDate currDate;

    /**
     * boolean variable indicating whether the calendar is displaying the Day View
     */
    private boolean dayViewDisplayed;

    private final DollarRecognizer recognizer;

    private Animation animation;

    private boolean animationMode;

    /**
     * Creates new object and assigns them to each class variable
     */
    public CalModel() {

        animation = new Animation(this);
        animationMode = false;
        eventList = new HashMap<>();
        currDate = LocalDate.now();
        dayViewDisplayed = true;
        bottomLabel = new JLabel();
        monthView = new MonthView(new MonthModel(this), new JPanel(new GridLayout(1, 1)));
        openDialogs = new ArrayList<>();
        recognizer = new DollarRecognizer();

        dayView = new DayView(this);
    }

    //All event related functions

    /**
     * Adds an event to the eventList hashmap
     * and determines it's position in the array list
     */
    public void addEvent(EventModel newEventModel) {
        boolean added = false;
        LocalDate newEventDate = newEventModel.getEventStartDate();

        //if the ArrayList at the given date is null, create a new ArrayList
        //and add the event to the front
        if (eventList.get(newEventDate) == null) {
            eventList.put(newEventDate, new ArrayList<>());
            eventList.get(newEventDate).add(newEventModel);
            added = true;
        }

        //determining where in the ArrayList to add the event
        for (int i = 0; i < eventList.get(newEventDate).size(); ++i) {
            //if new event occurs before current event
            if (newEventModel.getEventStartTime().getHour() < eventList.get(newEventDate).get(i).getEventStartTime().getHour()
            && !added) {
                eventList.get(newEventDate).add(i, newEventModel);
                added = true;
                break;
            } else if (newEventModel.getEventStartTime().getMinute() < eventList.get(newEventDate).get(i).getEventStartTime().getHour() & !added) {
                eventList.get(newEventDate).add(i, newEventModel);
                added = true;
                break;
            }
        }

        //if the event hasn't been added yet (i.e. it occurs last out of all the events for the day)
        //then add it to the ArrayList
        if (!added) {
            eventList.get(newEventDate).add(newEventModel);
        }

        //refreshes events in both the DayView and the MonthView
        //which removes all the View components for each event and replaces them with new ones
        dayView.refreshEvents(eventList.get(currDate));
        monthView.refreshEvents();
    }

    /**
     * Removes a give event from the event list
     * 
     * @param removeEventModel the event to remove
     */
    public void removeEvent(EventModel removeEventModel) {
        LocalDate removeEventDate = removeEventModel.getEventStartDate();

        //Error checking - making sure an ArrayList exists at the given date
        //and that the event is on that date
        if (eventList.get(removeEventDate) == null) {
            throw new IllegalArgumentException("No events on this date.");
        } else if (!eventList.get(removeEventDate).contains(removeEventModel)) {
            throw new IllegalArgumentException("Event does not exist");
        }

        //removing the event from the date that it is on
        eventList.get(removeEventDate).remove(removeEventModel);

        //refreshes events in both the DayView and the MonthView
        //which removes all the View components for each event and replaces them with new ones
        dayView.refreshEvents(eventList.get(currDate));
        monthView.refreshEvents();
    }

    /**
     * Returns whether the CalModel object has the given event listed in its hashmap eventList
     */
    public boolean containsEvent(EventModel eventModel) {
        return (!(eventList.get(eventModel.getEventStartDate()) == null) && eventList.get(eventModel.getEventStartDate()).contains(eventModel));
    }

    /**
     * Returns an array list of all the events on a given date
     */
    public ArrayList<EventModel> getDateEvents(LocalDate localDate) {
        if (eventList.get(localDate) == null) {
            eventList.put(localDate, new ArrayList<>());
        }
        return eventList.get(localDate);
    }


    //all the methods to handle dialogs

    /**
     * Adds a dialog to the ArrayList
     */
    public void addOpenDialog(JDialog newDialog) {
        newDialog.setVisible(true);
        openDialogs.add(newDialog);
    }

    /**
     * Removes a dialog from the ArrayList
     */
    public void removedOpenDialog(JDialog removeDialog) {
        removeDialog.dispose();
        openDialogs.remove(removeDialog);
        for (JDialog d : openDialogs) {
            d.repaint();
        }
    }

    /**
     * Removes all dialogs
     */
    public void closeAllDialogs() {
        for (int i = 0; i < openDialogs.size(); ++i) {
            openDialogs.get(i).dispose();
            openDialogs.remove(i);
        }
    }

    /**
     * @return an ArrayList containing all the events for current date
     */
    public ArrayList<EventModel> getCurrDateEvents() {
        return getDateEvents(currDate);
    }

    //getters for DayView & MonthView
    public DayView getDayView() { return dayView; }
    public MonthView getMonthView() { return monthView; }

    //getter and setter for currDate
    public LocalDate getCurrDate() { return currDate; }
    public void setCurrDate(LocalDate currDate) { this.currDate = currDate; }

    //getter and setter for dayViewDisplayed
    public boolean getDayViewDisplayed() { return dayViewDisplayed; }
    public void setDayViewDisplayed(boolean dayViewDisplayed) {
        this.dayViewDisplayed = dayViewDisplayed;
    }

    //getter and setter for bottomLabel
    public JLabel getBottomLabel() { return bottomLabel; }
    public void setBottomLabel(String bottomLabelString) { bottomLabel.setText(bottomLabelString); }

    public DollarRecognizer getRecognizer() { return recognizer; }

    public Animation getAnimation() { return animation; }

    public boolean getAnimationMode() { return animationMode; }
    public void setAnimationMode(boolean animationMode) { this.animationMode = animationMode; }

}
