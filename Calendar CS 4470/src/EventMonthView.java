package main;

import javax.swing.*;
import java.awt.*;

public class EventMonthView extends JComponent {

    public static final int HEIGHT = 20;

    private EventModel eventModel;
    private final CalModel calModel;
    private  MonthComponentView compView;
    private int x;
    private int y;
    private int width;
    private int eventPosition;
    private Color darkColor;

    public EventMonthView(MonthComponentView compView, EventModel eventModel, MonthModel monthModel) {
        this.eventModel = eventModel;
        eventModel.setEventMonthView(this);
        this.compView = compView;
        this.calModel = monthModel.getCalModel();

        EventMonthController eventMonthController = new EventMonthController(this, calModel);
        eventMonthController.initiateListeners();
    
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if ((compView.getHeight() / HEIGHT) > eventPosition) {

            if (eventModel.getColor() == null) {
                eventModel.setColor(EventDayView.BLUE);
            }
            Color lightColor = eventModel.getColor();
            darkColor = getDarkColor(lightColor);

            width = compView.getWidth();

            int newX = x + 7;
            int newY = y + 22;

            newY += eventPosition * HEIGHT;
            if (compView.isHoliday()) {
                newY += 18;
            }

            g.setColor(lightColor);
            g.fillRect(newX, newY, width - 13, 18);
            g.setColor(darkColor);
            g.fillRect(newX, newY, 10, 18);

            g.setFont(MonthView.WEEKDAY_FONT);
            g.setColor(Color.BLACK);

            String titleText = eventModel.getTitle();
            if (eventModel.getTagStatus(EventModel.Tag.VACATION)) {
                titleText += " #vacation";
            }
            if (eventModel.getTagStatus(EventModel.Tag.WORK)) {
                titleText += " #work";
            }
            if (eventModel.getTagStatus(EventModel.Tag.SCHOOL)) {
                titleText += " #school";
            }
            if (eventModel.getTagStatus(EventModel.Tag.FAMILY)) {
                titleText += " #family";
            }

            FontMetrics fontMetrics = g.getFontMetrics(MonthView.WEEKDAY_FONT);
            int titleLength = fontMetrics.stringWidth(titleText);

            int i = titleText.length();
            while (titleLength > width - 30) {
                titleText = titleText.substring(0, i) + "...";
                titleLength = fontMetrics.stringWidth(titleText);
                --i;
            }

            g.drawString(titleText, newX + 16, newY + 13);

            this.setBounds(newX, newY, width - 13, 18);

            compView.getCompModel().getMonthModel().getGlassPane().repaint();
        }
    }

    public static Color getDarkColor(Color color) {
        if (color.equals(EventDayView.PURPLE)) {
            return EventDayView.DARK_PURPLE;
        } else if (color.equals(EventDayView.BLUE)) {
            return EventDayView.DARK_BLUE;
        } else if (color.equals(EventDayView.GREEN)) {
            return EventDayView.DARK_GREEN;
        } else if (color.equals(EventDayView.YELLOW)) {
            return EventDayView.DARK_YELLOW;
        } else if (color.equals(EventDayView.ORANGE)) {
            return EventDayView.DARK_ORANGE;
        } else {
            return EventDayView.DARK_RED;
        }
    }

    public int calculateX() { 
        int x = compView.getCompModel().calculateXValue(compView) + 7;
        return x;
    }

    public int calculateY() {
        int y = compView.getCompModel().calculateYValue(compView) + 22;

        if (compView.isHoliday()) {
            y += 18;
        }

        y += eventPosition * this.getHeight();

        return y;
    }

    public EventModel getEvent() { return eventModel; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) {this.y = y; }

    public void setWidth(int width) { this.width = width; }

    public void setEventPosition(int eventPosition) { 
        this.eventPosition = eventPosition;
     }

     public void setMonthCompView(MonthComponentView compView) { this.compView = compView; }

     public MonthComponentView getCompView() { return compView; }

}
