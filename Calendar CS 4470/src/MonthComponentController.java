package main;


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

public class MonthComponentController {
    private final MonthComponentView compView;
    private final MonthComponentModel monthCompModel;
    private final MonthModel monthModel;
    private final CalModel calModel;
    private final Animation animation;
    private boolean drawStroke;
    private boolean startAnimation;
    private boolean animationStarted;
    private boolean forward; 

    public MonthComponentController(MonthComponentView compView, MonthComponentModel monthCompModel) {
        this.compView = compView;
        this.monthCompModel = monthCompModel;
        this.monthModel = monthCompModel.getMonthModel();
        this.calModel = monthModel.getCalModel();
        this.drawStroke = false;
        this.startAnimation = false;
        this.forward = false;
        this.animationStarted = false;
        this.animation = calModel.getAnimation();
    }

    public void activateListeners() {
        compClickedOn();
    }

    public void compClickedOn() {

        compView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                if (e.getButton() == MouseEvent.BUTTON1)  {
                    if (e.getClickCount() == 2) {
                        LocalDate currDate = monthCompModel.getDate();
                        LocalDateTime newEventStart = LocalDateTime.of(currDate.getYear(), currDate.getMonthValue(), currDate.getDayOfMonth(), 12, 0);
                        LocalDateTime newEventEnd = newEventStart.plusMinutes(60);
                        DialogEventEdit dialogEventEdit = new DialogEventEdit(calModel, new EventModel(newEventStart, newEventEnd));
                        calModel.addOpenDialog(dialogEventEdit);
                    }
                    compView.refreshEvents();
                    compView.getMonthView().repaint();
                    compView.repaint();
                } 
            }
        });

        compView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    drawStroke = true;
                } else if (e.getButton() == MouseEvent.BUTTON1 && !calModel.getAnimationMode()) {
                    int x = compView.getCompModel().calculateXValue(compView) + e.getX();
                    Point mousePoint = new Point(x, 0);
                    animation.setMousePoint(mousePoint);
                    if (x < compView.getWidth()) {
                        animation.setMouseReleased(false);
                        startAnimation = true;
                        forward = false;
                    } else if (x > calModel.getMonthView().getScrollPane().getWidth() - compView.getWidth()) {
                        animation.setMouseReleased(false);
                        startAnimation = true;
                        forward = true;
                    }
                 }
            }
        });
        compView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                drawStroke = false;
                startAnimation = false;
                if (animation.getMousePoint() != null) {
                    animation.setMouseReleased(true);
                }
                monthModel.getGlassPane().clearPoints(compView);
                animationStarted = false;
            
            }
        });

        compView.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (drawStroke) {
                    monthModel.getGlassPane().addPoint(compView, e);
                }
                if (startAnimation) {
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
                        calModel.getMonthView().add(animation);
                        animation.startAnimation(true, forward);
                        calModel.getAnimation().repaint();
                        calModel.getMonthView().repaint();
                        JScrollBar vertical = calModel.getMonthView().getScrollPane().getVerticalScrollBar();
                        vertical.setValue(vertical.getValue() + 1);
                        vertical.setValue(vertical.getValue() - 1);
                    }
                    
                    int x = compView.getCompModel().calculateXValue(compView) + e.getX();
                    System.out.println("X value of mouse: " + x);
                    Point mousePoint = new Point(x, 0);
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


}
