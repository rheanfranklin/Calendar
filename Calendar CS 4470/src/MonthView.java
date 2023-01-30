package main;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MonthView extends JPanel implements Scrollable {
    private static final int MIN_WIDTH = 650;
    private static final int MIN_HEIGHT = 500;

    public static final String[] WEEK_DAYS = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    public static final Color WEEKDAY_DISPLAY_COLOR = new Color(183, 224, 255);
    public static final Font WEEKDAY_FONT = new Font("Courtier", Font.PLAIN, 10);
    public static final Font DATE_DISPLAY_FONT = new Font("Courtier", Font.PLAIN, 15);

    private ArrayList<MonthComponentView> monthComponents;
    private final MonthModel monthModel;
    private final JPanel panel;
    private final GlassPaneMonth glassPane;
    private boolean animationAdded;
    private JScrollPane scrollPane;
    private String dateDisplay;
    private LocalDate currDate;
    private int row;
    private int height;
    private int width;
    private int boxHeight;
    private int boxWidth;


    public MonthView(MonthModel monthModel, JPanel panel) {
        this.monthModel = monthModel;
        this.panel = panel;
        glassPane = monthModel.getGlassPane();
        
        row = 6;
        monthComponents = new ArrayList<>();
        currDate = monthModel.getCalModel().getCurrDate();
        dateDisplay = "";
        animationAdded = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        panel.repaint();

        if (scrollPane == null) {
            throw new IllegalArgumentException("ScrollPane set to null");
        }

        setUpDimensions();

        if (!currDate.getMonth().equals(monthModel.getCalModel().getCurrDate().getMonth())) {
            setUpDateDisplay();
            setUpMonthComp();
        }
        setCompXAndY();

        this.setBounds(0, 0, width, height);
        this.setLayout(new FlowLayout());

        g.setColor(CalModel.BACKGROUND_COLOR);
        g.fillRect(0,0,width, height);

        g.setFont(DATE_DISPLAY_FONT);
        g.setColor(Color.WHITE);

        int y = 70 + boxHeight;
        g.setColor(CalModel.FONT_COLOR);
        for (int i = 0; i < 6; ++i) {
            g.drawLine(0, y, width, y);
            y += boxHeight;
        }

        for (int i = 0; i < monthComponents.size(); ++i) {
            if (i >= (row * 7)) {
                break;
            }
            monthComponents.get(i).paintComponent(g);
            add(monthComponents.get(i));
        }

        g.setColor(CalModel.BACKGROUND_COLOR);
        g.fillRect(0,0,width, 50);

        g.setColor(Color.WHITE);
        g.setFont(DATE_DISPLAY_FONT);
        g.drawString(dateDisplay, 30, 30);

        g.setColor(WEEKDAY_DISPLAY_COLOR);
        g.fillRect(0, 50, width, 20);

        g.setFont(WEEKDAY_FONT);
        int x = 0;
        for (int i = 0; i < 7; ++i) {
            g.setColor(CalModel.FONT_COLOR);
            g.drawLine(x, 50, x, (boxHeight * 6) + 70);

            g.setColor(CalModel.BACKGROUND_COLOR);
            g.drawString(WEEK_DAYS[i], x + 7, 65);

            x += boxWidth;

        }
        add(glassPane);
        glassPane.setMonthView(this);
        glassPane.setVisible(true);
        glassPane.paintComponent(g);
        glassPane.setBounds(this.getBounds());
    }

    
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        setUpDimensions();
        setUpDateDisplay();
        initializeMonthComponents();
    }

    private void initializeMonthComponents() {
        for (int i = 0; i < 42; ++i) {
            monthComponents.add(new MonthComponentView(new MonthComponentModel(monthModel), this, monthModel));
            if (i < 7) {
                monthComponents.get(i).getCompModel().setRow(0);
            } else if (i < 14) {
                monthComponents.get(i).getCompModel().setRow(1);
            } else if (i < 21) {
                monthComponents.get(i).getCompModel().setRow(2);
            } else if (i < 28) {
                monthComponents.get(i).getCompModel().setRow(3);
            } else if (i < 35) {
                monthComponents.get(i).getCompModel().setRow(4);
            } else {
                monthComponents.get(i).getCompModel().setRow(5);
            }

            monthComponents.get(i).getCompModel().setCol(i % 7);
        }
        setUpMonthComp();
    }

    private void setUpMonthComp() {
        currDate = monthModel.getCalModel().getCurrDate();

        LocalDate firstOfMonth = LocalDate.of(currDate.getYear(), currDate.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(currDate.getYear(), currDate.plusMonths(1).getMonth(), 1).minusDays(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() == 7 ? 0 : firstOfMonth.getDayOfWeek().getValue();


        MonthComponentView monthComponent;

        for (int i = 0; i < lastOfMonth.getDayOfMonth(); ++i) {
            int j = i + dayOfWeek;
            monthComponent = monthComponents.get(j);
            monthComponent.getCompModel().setActive(true);
            monthComponent.getCompModel().setDate(firstOfMonth.plusDays(i));
            monthComponent.getCompModel().setEvents();
            monthComponent.getCompModel().setArrayPosition(j);
            monthComponent.setHeight(boxHeight);
            monthComponent.setWidth(boxWidth);

            if (j < 7) {
                row = 0;

            }


            if (i == lastOfMonth.getDayOfMonth() - 1) {
                if (j < 21) {
                    row = 3;
                } else if (j < 28) {
                    row = 4;
                } else if (j < 35) {
                    row = 5;
                } else if (j < 42) {
                    row = 6;
                }
            }
        }

        for (int i = 0; i < dayOfWeek; ++i) {
            monthComponent = monthComponents.get(i);
            monthComponent.getCompModel().setActive(false);
            monthComponent.getCompModel().setDate(firstOfMonth.minusDays(dayOfWeek - i));
            monthComponent.getCompModel().setEvents();
            monthComponent.getCompModel().setArrayPosition(i);
            monthComponent.setHeight(boxHeight);
            monthComponent.setWidth(boxWidth);
        }

        int j = 1;
        for (int i = (dayOfWeek + lastOfMonth.getDayOfMonth()); i < monthComponents.size(); ++i) {
            monthComponent = monthComponents.get(i);
            monthComponent.getCompModel().setActive(false);
            monthComponent.getCompModel().setDate(lastOfMonth.plusDays(j));
            monthComponent.getCompModel().setEvents();
            monthComponent.getCompModel().setArrayPosition(i);
            monthComponent.setHeight(boxHeight);
            monthComponent.setWidth(boxWidth);
            ++j;
        }

        setCompXAndY();
    }

    public void refreshEvents() { 
        for (MonthComponentView m : monthComponents) { 
            m.refreshEvents();
        }
        repaint();
    }

    public MonthComponentView getComponent(int x, int y) { 
        MonthComponentView returnComp = null;

        for (MonthComponentView m : monthComponents) {
            int compX = m.getCompModel().calculateXValue(m);
            int compY = m.getCompModel().calculateYValue(m);
            int height = m.getHeight();
            int width = m.getWidth();
            
            if ((compX <= x && (compX + width) >= x) && (compY <= y && (compY + height) >= y)) {
                returnComp = m;
            }
        }

        //System.out.println("Calc x & Y: " + x + " " + y);
        //System.out.println("AP: " + returnComp.getCompModel().getArrayPosition());
        
        //if (returnComp == null) {
        //    System.out.println("null");
        //} else {
        //    System.out.println(returnComp.getCompModel().getArrayPosition());
        //}

        return returnComp;
    }

    private void setUpDateDisplay() {
        currDate = monthModel.getCalModel().getCurrDate();
        dateDisplay = "" + currDate.getMonth();
        dateDisplay = dateDisplay.toLowerCase();
        dateDisplay = dateDisplay.substring(0, 1).toUpperCase() + dateDisplay.substring(1);
        dateDisplay = dateDisplay + " " + currDate.getYear();
    }

    private void setUpDimensions() {
        height = Math.max(scrollPane.getHeight(), MIN_HEIGHT);
        width = Math.max(scrollPane.getWidth(), MIN_WIDTH);

        boxWidth = width / 7;
        boxHeight = height / 7;

        height = (boxHeight * 6) + 70;

        refreshCompDimensions();
    }

    private void refreshCompDimensions() {
        for (MonthComponentView m : monthComponents) {
            m.setHeight(boxHeight);
            m.setWidth(boxWidth);
        }
    }

    private void setCompXAndY() {
        LocalDate firstOfMonth = LocalDate.of(currDate.getYear(), currDate.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(currDate.getYear(), currDate.plusMonths(1).getMonth(), 1).minusDays(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() == 7 ? 0 : firstOfMonth.getDayOfWeek().getValue();
        
        MonthComponentModel compModel;
        MonthComponentView monthComponent;
        
        for (int i = 0; i < lastOfMonth.getDayOfMonth(); ++i) {
            int j = i + dayOfWeek;

            monthComponent = monthComponents.get(j);

            compModel = monthComponent.getCompModel();
            int compDayOfWeek = compModel.getDate().getDayOfWeek().getValue() == 7 ? 0 : compModel.getDate().getDayOfWeek().getValue();
            monthComponent.setX(boxWidth * compDayOfWeek);

            int row;
            if (j < 7) {
                row = 0;
            } else if (j < 14) {
                row = 1;
            } else if (j < 21) {
                row = 2;
            } else if (j < 28) {
                row = 3;
            } else if (j < 35) {
                row = 4;
            } else {
                row = 5;
            }

            monthComponent.setY((boxHeight * row) + 70);

            if ((compDayOfWeek == 0 || compDayOfWeek == 6) && compModel.getActive()) {
                compModel.setWeekend(true);
            }
        }

        for (int i = 0; i < dayOfWeek; ++i) {
            monthComponent = monthComponents.get(i);
            compModel = monthComponent.getCompModel();

            int compDayOfWeek = compModel.getDate().getDayOfWeek().getValue() == 7 ? 0 : compModel.getDate().getDayOfWeek().getValue();
            monthComponent.setX(boxWidth * compDayOfWeek);
            monthComponent.setY(70);
        }

        for (int i = lastOfMonth.getDayOfMonth() + dayOfWeek; i < monthComponents.size(); ++i) {
            monthComponent = monthComponents.get(i);
            compModel = monthComponent.getCompModel();

            int compDayOfWeek = compModel.getDate().getDayOfWeek().getValue() == 7 ? 0 : compModel.getDate().getDayOfWeek().getValue();
            monthComponent.setX(compDayOfWeek * boxWidth);

            int row;
            if (i < 7) {
                row = 0;
            } else if (i < 14) {
                row = 1;
            } else if (i < 21) {
                row = 2;
            } else if (i < 28) {
                row = 3;
            } else if (i < 35) {
                row = 4;
            } else {
                row = 5;
            }


            monthComponent.setY((boxHeight * row) + 70);

        }
    }

    public JScrollPane getScrollPane() { return scrollPane; }

    public void setAnimationAdded(boolean animationAdded) { this.animationAdded = animationAdded; }
    public boolean getAnimationAdded() { return animationAdded; }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(450, 800);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
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
        return new Dimension(Math.max(scrollPane.getWidth(), MIN_WIDTH), Math.max(scrollPane.getHeight(), MIN_HEIGHT));
    }

    public JPanel getPanel() { return panel; }

    public MonthModel getMonthModel() { return monthModel; }


}
