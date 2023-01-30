package main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.geom.Point2D;

public class MonthComponentView extends JComponent {
    private final static Color WEEKEND_COLOR = new Color(80, 80, 80);
    private static final Font TODAY_FONT = new Font("Courtier", Font.BOLD, 10);

    private ArrayList<EventMonthView> addedMonthViews;
    private ArrayList<Point2D> drawStroke;
    private final MonthComponentModel compModel;
    private final MonthView monthView;
    private final MonthModel monthModel;
    private boolean isHoliday;
    private int height = 10;
    private int width = 10;
    private int x = 0;
    private int y = 0;

    public MonthComponentView(MonthComponentModel compModel, MonthView monthView, MonthModel monthModel) {
        this.compModel = compModel;
        this.monthView = monthView;
        this.monthModel = monthModel;
        this.addedMonthViews = new ArrayList<>();
        this.drawStroke = new ArrayList<>();
        isHoliday = false;
        MonthComponentController compController = new MonthComponentController(this, compModel);
        compController.activateListeners();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        compModel.setEvents();

        g.setFont(MonthView.WEEKDAY_FONT);
        this.setBounds(x, y, width, height);
        if (!compModel.getDate().equals(LocalDate.now())) {
            if (compModel.getActive()) {
                if (compModel.getWeekend()) {
                    g.setColor(WEEKEND_COLOR);
                } else {
                    g.setColor(CalModel.BACKGROUND_COLOR);
                }
                g.fillRect(x, y, width, height);

                g.setColor(Color.WHITE);
                g.drawRect(x, y, width, height);

            } else {
                g.setColor(CalModel.FONT_COLOR);
            }


            g.drawString("" + compModel.getDate().getDayOfMonth(), x + 4, y + 13);

            if (compModel.getHoliday() != null) {
                isHoliday = true;
                String holiday = compModel.getHoliday();
                g.setColor(EventDayView.GREEN);
                g.fillRoundRect(x + 4, y + 20, width - 7, 15, 10, 10);

                FontMetrics fontMetrics = g.getFontMetrics(MonthView.WEEKDAY_FONT);
                int holidayWidth = fontMetrics.stringWidth(holiday);

                int i = holiday.length();
                while (holidayWidth > width - 15) {
                    holiday = holiday.substring(0, i) + "...";
                    holidayWidth = fontMetrics.stringWidth(holiday);
                    --i;
                }

                g.setColor(Color.BLACK);
                g.drawString(holiday, x + 10, y + 31);
            } else {
                isHoliday = false;
            }
        } else {
            g.setColor(EventDayView.BLUE);
            g.drawRect(x + 1, y + 1, width - 2, height - 2);
            g.drawRect(x + 2, y + 2, width - 4, height - 4);
            g.drawRect(x + 3, y + 3, width - 6, height - 6);
            g.setFont(TODAY_FONT);

            g.drawString("" + compModel.getDate().getDayOfMonth(), x + 8, y + 15);

            if (compModel.getHoliday() != null) {
                g.drawString(compModel.getHoliday(), x + 8, y + 25);
                isHoliday = true;
            } else {
                isHoliday = false;
            }
        }

        for (Point2D p : drawStroke) {
            g.setColor(Color.WHITE);
            g.fillOval((int)p.getX(), (int)p.getY(), 5, 5);
        }

    
        for (EventModel e : compModel.getEvents()) {
            if (e.getEventMonthView() != null) {
                e.getEventMonthView().paintComponent(g);
            }
        }

    }

    public void addEvent(EventMonthView addView) {
        addedMonthViews.add(addView);
        add(addView);
        repaint();
    }

    public void removeEvent(EventMonthView removeView) { 
        addedMonthViews.remove(removeView);
        remove(removeView);
        repaint();
    }

    public void refreshEvents() {
        compModel.setEvents();
        ArrayList<EventModel> eventList = compModel.getEvents();

        for (EventMonthView e : addedMonthViews) {
            remove(e);
        }

        addedMonthViews = new ArrayList<>();
        refreshEventViews(eventList);
        repaint();
    }


    public void refreshEventViews(ArrayList<EventModel> addEvents) { 
        int i = 0;
        for (EventModel e : addEvents) {
            EventMonthView eventMonthView = new EventMonthView(this, e, monthModel);
            eventMonthView.setEventPosition(i);
            addedMonthViews.add(eventMonthView);
            add(eventMonthView);
            ++i;
        }
    }

    public boolean isHoliday() { return isHoliday; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }


    public int getXValue() { return x;}
    public void setX(int x) { this.x = x; }
    
    public int getYValue() { return y; }
    public void setY(int y) { this.y = y; }

    public void addStroke(ArrayList<Point2D> drawStroke) { this.drawStroke = drawStroke; }
    public void clearStroke() { drawStroke = new ArrayList<>(); }

    public MonthComponentModel getCompModel() { return compModel; }

    public MonthView getMonthView() { return monthView; }





}
