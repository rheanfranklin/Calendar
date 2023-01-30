package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Point2D;

import java.awt.event.MouseEvent;

public class GlassPaneMonth extends JComponent {
    private ArrayList<Point2D> stroke;
    private MonthView monthView;
    private DollarRecognizer recognizer;
    private CalModel calModel;
    private boolean horizontal; 

    public GlassPaneMonth(CalModel calModel) {
        this.calModel = calModel;
        stroke = new ArrayList<>();
        recognizer = new DollarRecognizer();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);

        for(Point2D p : stroke) {
            g.fillOval((int)p.getX(), (int)p.getY(), 5, 5);
        }
        if(monthView != null) {
            setBounds(monthView.getBounds());
        }
    }


    public void addPoint(MonthComponentView compView, MouseEvent e) {
        int x = e.getX() + compView.getCompModel().calculateXValue(compView);
        int y = e.getY() + compView.getCompModel().calculateYValue(compView);
        Point point = new Point(x, y);
        stroke.add(point);
    }

    public void addPoint(EventMonthView eventView, MouseEvent e) {
        int x = e.getX() + eventView.calculateX();
        int y = e.getY() + eventView.calculateY();
        Point point = new Point(x, y);
        stroke.add(point);
    }

    public void clearPoints(MonthComponentView compView) { 
        if (stroke.size() > 0) {
            Result result = recognizer.recognize(stroke);
            if (result.getMatchedTemplate() != null) {
                determineIfHorizontal();
                CommandExecutioner command = new CommandExecutioner(result.getName(), calModel);
                command.setGlassPane(this);
                command.executeCommand(compView);
            }
        }

        stroke = new ArrayList<>(); 
        repaint();
    }

    public void clearPoints(EventMonthView eventView) { 
        if (stroke.size() > 0) {
            Result result = recognizer.recognize(stroke);
            if (result.getMatchedTemplate() != null) {
                determineIfHorizontal();
                CommandExecutioner command = new CommandExecutioner(result.getName(), calModel);
                command.setGlassPane(this);
                command.executeCommand(eventView);
            }
        }

        stroke = new ArrayList<>(); 
        repaint();
    }

    public void determineIfHorizontal() { 
        double maxX = 0;
        double minX = 0;
        double maxY = 0;
        double minY = 0;

        for (Point2D p : stroke) {
            if (p.getX() > maxX) {
                maxX = p.getX();
            }
            if (p.getX() < minX) {
                minX = p.getX();
            }
            if (p.getY() > maxY) {
                maxY = p.getY();
            }
            if (p.getY() < minY) { 
                minY = p.getY();
            }
        }

        double deltaX = maxX - minX;
        double deltaY = maxY - minY;

        if (deltaX > deltaY) {
            horizontal = true;
        } else {
            horizontal = false;
        }
    }

    public void setMonthView(MonthView monthView) { this.monthView = monthView; }

    public boolean getHorizontal() { return horizontal; }
}
