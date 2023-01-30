package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Point2D;


public class GlassPaneDay extends JComponent {
    private ArrayList<Point2D> stroke;
    private DayView dayView;
    private DollarRecognizer recognizer;
    private CalModel calModel;

    public GlassPaneDay(CalModel calModel) {
        this.calModel = calModel;
        this.recognizer = new DollarRecognizer();
        this.stroke = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);

        for(Point2D p : stroke) {
            g.fillOval((int)p.getX(), (int)p.getY(), 5, 5);
        }
        if(dayView != null) {
            setBounds(dayView.getBounds());
        }
    }

    public void addPoint(EventDayView eventView, Point point) {
        point.setLocation(point.getX() + eventView.getX(), point.getY() + eventView.getY());
        stroke.add(point);
    }

    public void addPoint(Point point) {
        stroke.add(point);
    }

    public void clearStroke(EventDayView eventView) {
        if (stroke.size() > 0) {
            Result result = recognizer.recognize(stroke);
            if (result.getMatchedTemplate() != null) {
                CommandExecutioner command = new CommandExecutioner(result.getName(), calModel);
                command.executeCommand(eventView);
            }
        }

        stroke = new ArrayList<>();
    }

    public void clearStroke(DayView dayView) {
        if (stroke.size() > 0) {
            Result result = recognizer.recognize(stroke);
            if (result.getMatchedTemplate() != null) {
                CommandExecutioner command = new CommandExecutioner(result.getName(), calModel);
                command.executeSimilarDayCommands();
            }
        } 
        stroke = new ArrayList<>();
    }

    

    public void setDayView(DayView dayView) { this.dayView = dayView; }

}
