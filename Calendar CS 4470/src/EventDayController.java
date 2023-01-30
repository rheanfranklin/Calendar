package main;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A class dedicated to handling user interaction with the EventDayView
 */
public class EventDayController {

    private EventDayView eventDayView;
    private CalModel calModel;
    private int currY;
    private GlassPaneDay glassPane;

    private boolean commandMode;

    public EventDayController(EventDayView eventDayView, CalModel calModel) {
        this.eventDayView = eventDayView;
        this.calModel = calModel;
        glassPane = calModel.getDayView().getDayModel().getGlassPane();
        commandMode = false;
    }

    public void initializeListeners() { 
        draggedOnEvent();
        clickedOnEvent();
    }

    public void clickedOnEvent() {
        eventDayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    commandMode = false;
                    if (e.getClickCount() == 2) {
                        DialogEventSummary dialogEventSummary = new DialogEventSummary(calModel, eventDayView.getEvent());
                        calModel.addOpenDialog(dialogEventSummary);
                        dialogEventSummary.setVisible(true);
                    }
                }
            }
        });
    }

    public void draggedOnEvent() {
        eventDayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    commandMode = true;
                } else {
                    commandMode = false;
                    currY = e.getY();
                }
            }
        });

        eventDayView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                commandMode = false;
                glassPane.clearStroke(eventDayView);
                glassPane.repaint();
            }
        });

        eventDayView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!commandMode) {
                    if (e.getY() > currY + 2) {
                         eventDayView.getEvent().lowerExistingEventDragged(e.getY(), currY);
                        currY = e.getY();
                        eventDayView.update();
                    }
                    if (e.getY() + 2 < currY) {
                        eventDayView.getEvent().raiseExistingEventDragged(currY, e.getY());
                        currY = e.getY();
                        eventDayView.update();
                    }
                } else {
                    glassPane.addPoint(eventDayView, new Point(e.getX(), e.getY()));
                    glassPane.repaint();
                }
            }
        });
    }






}
