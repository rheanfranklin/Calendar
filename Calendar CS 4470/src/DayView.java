package main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * A class that draws the dayView on the screen
 */
public class DayView extends JPanel implements Scrollable {

    public static final int LINE_SPACING = 35;
    public static final int DAY_VIEW_LENGTH = 450;
    private static final int LINE_LENGTH = 363;
    private static final int LINE_START_X_VALUE = 67;
    private static final int HORIZONTAL_BORDER_SPACING = 10;
    public static final int EVENT_START_Y_VALUE = 44;
    private static final Color BACKGROUND_COLOR = new Color(50,50, 50);
    private static final Color FONT_COLOR = new Color(150, 150, 150);

    private DayModel dayModel;
    private GlassPaneDay glassPane;
    private CalModel calModel;
    private boolean animationAdded;
    private ArrayList<EventDayView> addedEventDayViews;

    public DayView(CalModel calModel) {
        this.calModel = calModel;
        this.dayModel = new DayModel(calModel);
        this.glassPane = dayModel.getGlassPane();
        DayController dayController = new DayController(dayModel);
        dayController.setDayView(this);
        dayController.initializeListeners();
        animationAdded = false;
        addedEventDayViews = new ArrayList<>();
    }


    /**
     * Draws the DayView component on the screen
     */
    @Override
    public void paintComponent(Graphics g) {

        LocalDate currDate = dayModel.getCurrDate();

        super.paintComponent(g);

        //setting up DayView border and background
        g.setColor(BACKGROUND_COLOR);
        g.fillRoundRect(1, 1, DAY_VIEW_LENGTH - 1, 879 - 1, 15, 15);

        //setting up white border
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, DAY_VIEW_LENGTH, 880, 20, 20);
        //this.setBounds(0, 0, 450, 800);

        //setting up date display at top
        g.setColor(FONT_COLOR);
        Font dateDisplayFont = new Font("Courtier", Font.PLAIN, 13);
        g.setFont(dateDisplayFont);

        g.drawString("" + currDate.getDayOfWeek()
                + ", " + currDate.getMonth()
                + " " + currDate.getDayOfMonth()
                + ", " + currDate.getYear(), 15, 20
        );


        //Adding guides for time of day

        Font timeGuideFont = new Font("Courtier", Font.PLAIN, 13);
        g.setFont(timeGuideFont);


        int hour = 0;
        boolean isItAM = true;
        String timeDisplay;
        long y = 50;
        for (int i = 0; i < 24; ++i) {

            if (hour < 10) {
                timeDisplay = "  ";
            } else {
                timeDisplay = "";
            }

            if (hour >= 12) {
                isItAM = false;
            } else {
                isItAM = true;
            }

            int displayHour = hour;

            if (displayHour > 12) {
                displayHour -= 12;
            } else if (displayHour == 0) {
                displayHour = 12;
            }

            if (isItAM) {
                timeDisplay = timeDisplay + displayHour + " AM";
            } else {
                timeDisplay = timeDisplay + displayHour + " PM";
            }

            g.drawString(timeDisplay, HORIZONTAL_BORDER_SPACING, (int) y);
            g.drawLine(LINE_START_X_VALUE, (int) (y - 7), LINE_START_X_VALUE + LINE_LENGTH, (int) (y - 7));

            dayModel.putYValuesPerHour(i, y - 7);

            //drawing the line showing current time
            //determining if currDate is today
            if (currDate.equals(LocalDate.now())) {
                //determining if we are in the process of drawing the current hour
                if (LocalDateTime.now().getHour() == hour) {
                    //determining if we are in the correct AM/PM
                    if ((LocalDateTime.now().getHour() >= 12 && !isItAM) || (LocalDateTime.now().getHour() < 12 && isItAM)) {
                        //determining where within hour display spacing to put the line
                        double ratio = LocalDateTime.now().getMinute() / (double) 60;
                        long addY = Math.round(ratio * LINE_SPACING);
                        Font currTimeFont = new Font("Courtier", Font.PLAIN, 10);

                        //drawing the line
                        g.setFont(currTimeFont);
                        g.setColor(Color.RED);
                        g.drawLine(LINE_START_X_VALUE, (int) (y - 7 + addY), LINE_START_X_VALUE + LINE_LENGTH, (int) (y - 7 + addY));
                        g.fillOval(LINE_START_X_VALUE - 7, (int) (y - 11 + addY), 7, 7);

                        //drawing the current time
                        String currTime = (LocalDateTime.now().getHour() % 12) + ":" +
                                (LocalDateTime.now().getMinute() < 10 ? "0" + LocalDateTime.now().getMinute() : LocalDateTime.now().getMinute());

                        if (isItAM) {
                            currTime = currTime + " AM";
                        } else {
                            currTime = currTime + " PM";
                        }

                        g.drawString(currTime, LINE_START_X_VALUE + 3, (int) (y - 13 + addY));

                        //resetting the font & color
                        g.setFont(timeGuideFont);
                        g.setColor(FONT_COLOR);
                    }
                }
            }

            y += LINE_SPACING;

            ++hour;
        }
        for (EventDayView e : addedEventDayViews) {
            e.paintComponent(g);
        }
        add(glassPane);
        glassPane.setVisible(true);
        glassPane.paintComponent(g);
        glassPane.setBounds(this.getBounds());

    }

    public void refreshEvents(ArrayList<EventModel> eventList) {
        for (int i = 0; i < addedEventDayViews.size(); ++i) {
            remove(addedEventDayViews.get(i));
        }
        addedEventDayViews = new ArrayList<>();
        refreshEventViews(eventList);
        repaint();
    }

    private void refreshEventViews(ArrayList<EventModel> addEvents) {

        double eventYStartValue;
        double eventYEndValue;
        double eventLength;
        double pixelPerMin = (double)LINE_SPACING / (double)60;

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        for (int i = 0; i < addEvents.size(); ++i) {
            startDateTime = addEvents.get(i).getEventStartTime();
            endDateTime = addEvents.get(i).getEventEndTime();

            eventLength = (endDateTime.getHour() - startDateTime.getHour()) * 60;
            eventLength = eventLength - startDateTime.getMinute() + endDateTime.getMinute();

            eventYStartValue = EVENT_START_Y_VALUE + (startDateTime.getHour() * LINE_SPACING);
            eventYStartValue += startDateTime.getMinute() * pixelPerMin;

            eventYEndValue = eventYStartValue + (eventLength * pixelPerMin);


            int counter = 0;
            int beforeCounter = 0;
            if (addEvents.size() > 1) {
                for (int j = 0; j < addEvents.size(); ++j) {
                    if (j != i) {
                        if ((addEvents.get(i).getEventStartTime().compareTo(addEvents.get(j).getEventEndTime()) < 0
                                && addEvents.get(i).getEventEndTime().compareTo(addEvents.get(j).getEventEndTime()) >= 0)
                                || (addEvents.get(j).getEventStartTime().compareTo(addEvents.get(i).getEventEndTime()) < 0
                                && addEvents.get(j).getEventEndTime().compareTo(addEvents.get(i).getEventEndTime()) >= 0)) {
                            ++counter;
                            if (j >= i) {
                                ++beforeCounter;
                            }
                        }
                    }
                }
            }
            int length;
            if (counter != 0) {
                length = LINE_LENGTH / (counter + 1);
            } else {
                length = LINE_LENGTH;
            }


            EventDayView eventDayView = new EventDayView(LINE_START_X_VALUE + beforeCounter * length, (int)eventYStartValue,
                    (int) Math.abs(eventYEndValue - eventYStartValue), length, addEvents.get(i), calModel);
            addEvents.get(i).setEventDayView(eventDayView);
            addedEventDayViews.add(eventDayView);
            add(eventDayView);
        }
    }



    public DayModel getDayModel() { return dayModel; }

    public void setAnimationAdded(boolean animationAdded) { this.animationAdded = animationAdded; }
    public boolean getAnimationAdded() { return animationAdded; }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(450, 800);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(450, 1000);
    }
    
}
