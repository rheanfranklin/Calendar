package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EventMonthController {

    private EventMonthView eventMonthView;
    private CalModel calModel;
    private int viewX;
    private int viewY;
    private MonthComponentView oldCompView;
    private GlassPaneMonth glassPane;
    private EventModel event;
    private EventModel oldEvent;
    private LocalDate startDate;
    private boolean commandMode;

    public EventMonthController(EventMonthView eventMonthView, CalModel calModel) {
        this.eventMonthView = eventMonthView;
        this.calModel = calModel;
        glassPane = eventMonthView.getCompView().getMonthView().getMonthModel().getGlassPane();
        commandMode = false;
    }

    public void initiateListeners() {
        eventMonthViewClickedOn();
        draggedOnEvent();
    }

    public void eventMonthViewClickedOn() {
        eventMonthView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    DialogEventSummary dialogEventSummary = new DialogEventSummary(calModel, eventMonthView.getEvent());
                    calModel.addOpenDialog(dialogEventSummary);
                    dialogEventSummary.setVisible(true);
                }
            }
        });
    }

    public void draggedOnEvent() { 

        eventMonthView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    commandMode = false;
                    viewX = eventMonthView.getCompView().getCompModel().calculateXValue(eventMonthView.getCompView());
                    viewY = eventMonthView.getCompView().getCompModel().calculateYValue(eventMonthView.getCompView());
                    oldCompView = eventMonthView.getCompView();
                    oldEvent = eventMonthView.getEvent();
                    event = new EventModel(oldEvent);
                    startDate = eventMonthView.getCompView().getCompModel().getDate();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    commandMode = true;
                }
            }
        });

        eventMonthView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!commandMode) {
                    CalModel calModel = eventMonthView.getCompView().getCompModel().getMonthModel().getCalModel();
                
                    calModel.getDayView().refreshEvents(calModel.getDateEvents(eventMonthView.getCompView().getCompModel().getDate()));
                    calModel.getDayView().refreshEvents(calModel.getDateEvents(startDate));
                }
                commandMode = false;
                glassPane.clearPoints(eventMonthView);

                //eventMonthView.getCompView().getMonthView().refreshEvents();
                //System.out.println(event.getEventStartDate());

            }
        });


        //eventMonthView.addMouseMotionListener(new MouseAdapter() {
          //  @Override
            //public void mouseDragged(MouseEvent e) {
              //  super.mouseDragged(e);
                //MonthComponentView newCompView = eventMonthView.getCompView().getMonthView().getComponent(e.getX() + viewX, e.getY() + viewY);
                //System.out.println(newCompView.getCompModel().getArrayPosition());
                //
                //System.out.println("e: " + e.getX() + " " + e.getY());
                //System.out.println("viewX&Y: " + viewX + " " + viewY);
                //if (newCompView != null && !oldCompView.equals(newCompView)) {
                //    EventModel event = eventMonthView.getEvent();
                //    long eventDuration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());
                //    LocalDate oldDate = newCompView.getCompModel().getDate();
                //    LocalDateTime newDate = LocalDateTime.of(oldDate.getYear(), oldDate.getMonth(), oldDate.getDayOfMonth(), event.getEventStartTime().getHour(), event.getEventStartTime().getMinute());
                //    
                //    event.setEventStart(newDate);
                //    event.setEventEnd(newDate.plusMinutes(eventDuration));
                //    System.out.println("" + event.getEventStartDate());
                //    
                //   oldCompView.getCompModel().setEvents();
                //    oldCompView.remove(eventMonthView);

                  //  newCompView.add(eventMonthView);
                    //eventMonthView.setMonthCompView(newCompView);
                    //oldCompView = newCompView;

                    //eventMonthView.repaint();
                    //oldCompView.repaint();
                    //newCompView.repaint();
                //}

            //}
        //});

        eventMonthView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!commandMode) {

                    long eventDuration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());
                    LocalDateTime oldDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), event.getEventStartTime().getHour(), event.getEventStartTime().getMinute());
                    CalModel calModel = eventMonthView.getCompView().getCompModel().getMonthModel().getCalModel();
 

                    if (e.getX() < 0) {
                        event.setEventStart(oldDate.minusDays(1));
                        event.setEventEnd(oldDate.minusDays(1));
                        calModel.removeEvent(oldEvent);
                        calModel.addEvent(event);
                    //eventMonthView.getCompView().refreshEvents();


                    } else if (e.getX() > eventMonthView.getCompView().getWidth()) {
                        event.setEventStart(oldDate.plusDays(1));
                        event.setEventEnd(oldDate.plusDays(1));

                        calModel.removeEvent(oldEvent);
                        calModel.addEvent(event);
                    //eventMonthView.getCompView().refreshEvents();

                    } else if (e.getY() < 0) {
                        event.setEventStart(oldDate.minusDays(7));
                        event.setEventEnd(oldDate.minusDays(7));

                        calModel.removeEvent(oldEvent);
                        calModel.addEvent(event);
                    //eventMonthView.getCompView().refreshEvents();

                    } else if (e.getY() > eventMonthView.getCompView().getHeight()) {
                        event.setEventStart(oldDate.plusDays(7));
                        event.setEventEnd(oldDate.plusDays(7));

                        calModel.removeEvent(oldEvent);
                        calModel.addEvent(event);
                    //eventMonthView.getCompView().refreshEvents();

                    }
                } else {
                    glassPane.addPoint(eventMonthView, e);
                }
                
                //MonthComponentView newCompView = eventMonthView.getCompView().getMonthView().getComponent(e.getX() + viewX, e.getY() + viewY);

                //if (newCompView == null) {
                //    System.out.println("null");
                //} else {
                //    for (EventModel f : calModel.getDateEvents(newCompView.getCompModel().getDate())) {
                 //       System.out.println("New date event: " + f.getTitle() + " " + f.getEventStartDate());
                //    }
                //}

                //for (EventModel f : calModel.getDateEvents(startDate)) {
                //    System.out.println("Old date event: " + f.getTitle() + " " + f.getEventStartDate());
                //}


                //if (newCompView != null && !oldCompView.equals(newCompView)) {
                //        long eventDuration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());
                //        LocalDate oldDate = newCompView.getCompModel().getDate();
                //        LocalDateTime newDate = LocalDateTime.of(oldDate.getYear(), oldDate.getMonth(), oldDate.getDayOfMonth(), event.getEventStartTime().getHour(), event.getEventStartTime().getMinute());
                //        CalModel calModel = eventMonthView.getCompView().getCompModel().getMonthModel().getCalModel();
                        

                //        event.setEventStart(newDate);
                //        event.setEventEnd(newDate.plusMinutes(eventDuration));
                //        System.out.println(event.getTitle());
                //        System.out.println(event.getEventStartDate());
                        //System.out.println("" + event.getEventStartDate());
                    
                 //       oldCompView.getCompModel().setEvents();
                        //oldCompView.remove(eventMonthView);

                 //       oldCompView.removeEvent(eventMonthView);
                   //     newCompView.addEvent(eventMonthView);
                        
                     //   System.out.println("Calculated new X&Y: " + newCompView.getWidth() + " " +  (newCompView.getCompModel().getRow() * newCompView.getHeight() + 70));
                        
                        //if (e.getY() < 0) {
                        //    newCompView.setY(0 - newCompView.getHeight());
                        //} else if (e.getY() > newCompView.getHeight()) {
                        //    newCompView.setY(newCompView.getHeight());
                        //} else if (e.getX() < 0) {
                        //    newCompView.setX(0 - newCompView.getWidth());
                        //} else if (e.getX() > newCompView.getWidth()) {
                        //    newCompView.setX(newCompView.getWidth());
                        //}
                        //eventMonthView.setMonthCompView(newCompView);
                        //oldCompView = newCompView;
    
                       // eventMonthView.repaint();
                        //oldCompView.repaint();
                        //newCompView.repaint();
                //}
            }
            
        });

    }
}
