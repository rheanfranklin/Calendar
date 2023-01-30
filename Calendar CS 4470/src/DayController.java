package main;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.EventObject;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JScrollBar;

import java.awt.*;

/**
 * A class that manages user interaction with the DayView
 */

public class DayController {
    /**
     * DayView that the controller is adding listeners to 
     */
    private DayView dayView;

    /**
     * Model of the day that the DayView pulls data from
     */
    private final DayModel dayModel;

    /**
     * Model of the calendar
     */
    private final CalModel calModel;

    private final Animation animation;

    private boolean commandMode;

    private GlassPaneDay glassPane;
    private boolean startAnimation;
    private boolean animationStarted;
    private boolean forward;


    public DayController(DayModel dayModel) {
        this.dayModel = dayModel;
        this.calModel = dayModel.getCalModel();
        this.animation = calModel.getAnimation();
        glassPane = dayModel.getGlassPane();
    
        commandMode = false;
        startAnimation = false;
        animationStarted = false;
        forward = false;
    }


    public void initializeListeners() { 
        clickedOnDayView();
        draggedOnDayView();
    }

    private void clickedOnDayView() {
        if (dayView == null) {
            throw new IllegalArgumentException("Day view is set to null.");
        }

        dayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    commandMode = false;
                    if (e.getClickCount() == 2) {
                        LocalDate currDate = calModel.getCurrDate();
                        LocalDateTime newEventStart = LocalDateTime.of(currDate.getYear(), currDate.getMonthValue(), currDate.getDayOfMonth(), dayModel.getHour(e.getY()),0, 0);
                        LocalDateTime newEventEnd = newEventStart.plusMinutes(60);
                        DialogEventEdit dialogEventEdit = new DialogEventEdit(calModel, new EventModel(newEventStart, newEventEnd));
                        calModel.addOpenDialog(dialogEventEdit);
                        dayView.refreshEvents(calModel.getCurrDateEvents());
                    }     
                } 
            }
        });
    }

    private void draggedOnDayView() {

        if (dayView == null) {
            throw new IllegalArgumentException("Day view is set to null.");
        }
        
        final EventModel[] event = new EventModel[1];
        final boolean[] added = {false};

        dayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    commandMode = false;
                    Point mousePoint = new Point(e.getX(), 0);
                    if (e.getX() < 50) {
                        System.out.println("I have been summoned");

                        animation.setMousePoint(mousePoint);
                        startAnimation = true;
                        forward = false;
                    } else if (e.getX() > DayView.DAY_VIEW_LENGTH - 50) {
                        System.out.println("yes, your majesty?");

                        animation.setMousePoint(mousePoint);
                        startAnimation = true;
                        forward = true;
                    } else {
                        startAnimation = false;
                        event[0] = dayModel.createEventFromMousePressToDrag(e.getY());
                        added[0] = false;
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    commandMode = true;
                }
            }
        });

        dayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                commandMode = false;
                glassPane.clearStroke(dayView);
                glassPane.repaint();
                animationStarted = false;
                startAnimation = false;
                forward = false;
                if (animation.getMousePoint() != null) {
                    animation.setMouseReleased(true);
                }
                
            }
        });

        dayView.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!commandMode && !startAnimation) {
                    if (!added[0]) {
                        calModel.addEvent(event[0]);
                        added[0] = true;
                    }
                    dayModel.updatedDraggedEventEndTime(e.getY(), event[0]);
                    dayView.refreshEvents(calModel.getCurrDateEvents());
                } else if (commandMode) {
                    glassPane.addPoint(new Point(e.getX(), e.getY()));
                    glassPane.repaint();
                } else if (startAnimation) {
                    if (!animationStarted) {
                        if (!animationStarted) {
                            animationStarted = true;
    
                            BufferedImage frontImage;
                            BufferedImage backImage;
    
                            if (forward) {
                                if (calModel.getDayViewDisplayed()) {
                                    frontImage = makeOffScreenImage(calModel.getDayView());
                                    
                                    
                                    calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
                                    calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
    
                                    backImage = makeOffScreenImage(calModel.getDayView());
    
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusDays(-1));
                                    calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
                                } else {
    
                                    frontImage = makeOffScreenImage(calModel.getMonthView());
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
                                    calModel.getMonthView().refreshEvents();
    
                                    backImage = makeOffScreenImage(calModel.getMonthView());
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusMonths(-1));
                                    calModel.getMonthView().refreshEvents();
    
                                }
                            } else {
                                if (calModel.getDayViewDisplayed()) {
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusDays(-1));
                                    calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
    
                                    frontImage = makeOffScreenImage(calModel.getDayView());
    
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
                                    calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
    
                                    backImage = makeOffScreenImage(calModel.getDayView());
    
                                } else {
                                    calModel.setCurrDate(calModel.getCurrDate().plusMonths(-1));
                                    calModel.getMonthView().refreshEvents();
    
                                    frontImage = makeOffScreenImage(calModel.getMonthView());
    
    
                                    calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
                                    calModel.getMonthView().refreshEvents();
    
                                    backImage = makeOffScreenImage(calModel.getMonthView());
    
                                }
                            }
                            calModel.setAnimationMode(true);
                            animation.setBackImage(backImage);
                            animation.setFrontImage(frontImage);
                            calModel.getDayView().add(animation);
                            animation.startAnimation(true, forward);
                            calModel.getAnimation().repaint();
                            calModel.getDayView().repaint();
                            JScrollBar vertical = calModel.getMonthView().getScrollPane().getVerticalScrollBar();
                            vertical.setValue(vertical.getValue() + 1);
                            vertical.setValue(vertical.getValue() - 1);
                        }
                        System.out.println("Mouse point x: " + e.getX());
                    }
                }
                if (startAnimation) {
                    Point mousePoint = new Point(e.getX(), 0);
                    animation.setMousePoint(mousePoint);
                    animation.repaint();
                }
            }
        });
    }


    public BufferedImage makeOffScreenImage(JComponent source) {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());

        Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();

        source.paint(offscreenGraphics);

        return offscreenImage;
    }
    public void setDayView(DayView dayView) { this.dayView = dayView; }
}
