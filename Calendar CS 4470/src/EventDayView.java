package main;

import javax.swing.*;
import java.awt.*;

public class EventDayView extends JComponent {
    public final static Color PURPLE = new Color(173, 150 ,255);
    public final static Color DARK_PURPLE = new Color(133, 110, 215);
    public final static Color BLUE = new Color(153, 204, 255);
    public final static Color DARK_BLUE = new Color(113, 164, 225);
    public final static Color GREEN = new Color(144, 208, 144);
    public final static Color DARK_GREEN = new Color(104, 178, 104);
    public final static Color YELLOW = new Color(240, 230, 140);
    public final static Color DARK_YELLOW = new Color(215, 180, 90);
    public final static Color ORANGE = new Color(250, 160, 122);
    public final static Color DARK_ORANGE = new Color(210, 120, 82);
    public final static Color RED = new Color(205, 92, 92);
    public final static Color DARK_RED = new Color(163, 72, 72);

    private int x;
    private int y;
    private int height;
    private int width;
    private EventModel eventModel;

    private Color textColor;
    private Font font;

    public EventDayView(int x, int y, int height, int width, EventModel eventModel, CalModel calModel) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.eventModel = eventModel;
        textColor = Color.BLACK;
                //new Color(0,  76, 153);
        font = new Font("Courtier", Font.PLAIN, 11);
        EventDayController eventDayController = new EventDayController(this, calModel);
        eventDayController.initializeListeners();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(eventModel.getColor() == null ? BLUE : eventModel.getColor());

        g.fillRoundRect(x, y, width, height, 20, 20);
        this.setSize(width, height);
        this.setBounds(x, y, width, height);
        int textX = x + 10;

        g.setColor(textColor);
        g.setFont(font);
        if (this.getBounds().contains(textX, y + 26)) {
            String titleText = eventModel.getTitle();
            

            FontMetrics fontMetrics = g.getFontMetrics(MonthView.WEEKDAY_FONT);
            int titleLength = fontMetrics.stringWidth(titleText);

            if (eventModel.getTagStatus(EventModel.Tag.VACATION)) {
                titleText += " #vacation";
            } 
            if (eventModel.getTagStatus(EventModel.Tag.FAMILY)) {
                titleText += " #family";
            } 
            if (eventModel.getTagStatus(EventModel.Tag.WORK)) {
                titleText += " #work";
            } 
            if (eventModel.getTagStatus(EventModel.Tag.SCHOOL)) {
                titleText += " #school";
            } 

            int i = titleText.length();
            while (titleLength > width - 30) {
                titleText = titleText.substring(0, i) + "...";
                titleLength = fontMetrics.stringWidth(titleText);
                --i;
            }

            g.drawString(titleText, textX, y + 15);
        }

        

        if (this.getBounds().contains(textX, y + 41)) {
            g.drawString(eventModel.getLocation(), textX, y + 30);
        }

        if (this.getBounds().contains(textX, y + 56)) {
            g.drawString(eventModel.getDescription(), textX, y + 45);
        }
        
    }

    public EventModel getEvent() { return eventModel; }

    public void update() {
        y = DayModel.getEventStartYValue(eventModel);
        repaint();
    }


    public void setTextColor(Color color) {
        textColor = color;
    }

    public void setX(int x) { this.x = x; }

    public int getYValue() { return y; }
    public void setY(int y) { this.y = x;}


}
