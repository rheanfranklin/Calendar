package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.ViewportLayout;

public class CommandExecutioner {
    private String name;
    private CalModel calModel;
    private GlassPaneMonth glassPane;

    public CommandExecutioner(String name, CalModel calModel) {
        this.name = name;
        this.calModel = calModel;
    }



    public void executeCommand(EventMonthView eventView) {
        EventModel event = eventView.getEvent();
        if (name.equals("delete")) {
            calModel.removeEvent(eventView.getEvent());
        } else if (name.equals("star")) {
            event.setTag(EventModel.Tag.FAMILY, !event.getTagStatus(EventModel.Tag.FAMILY));
        } else if (name.equals("check")) {
            event.setTag(EventModel.Tag.VACATION, !event.getTagStatus(EventModel.Tag.VACATION));
        } else if (name.equals("pigtail")) {
            event.setTag(EventModel.Tag.WORK, !event.getTagStatus(EventModel.Tag.WORK));
        } else if (name.equals("x")) {
            event.setTag(EventModel.Tag.SCHOOL, !event.getTagStatus(EventModel.Tag.SCHOOL));
        } else if (name.equals("circle")) {
            ArrayList<EventModel> events = new ArrayList<>();
            events.addAll(calModel.getDateEvents(eventView.getEvent().getEventStartDate()));

            for (EventModel e : events) { 
                calModel.removeEvent(e);
            }
        } else if (name.equals("zig-zag")) {
            if (glassPane.getHorizontal()) {
        
                int dayOfWeek = event.getEventStartDate().getDayOfWeek().getValue() == 7 ? 0 : event.getEventStartDate().getDayOfWeek().getValue();
                int daysAfter = 6 - dayOfWeek;
                for (int i = 0; i < daysAfter; ++i) {
                    EventModel newEvent = new EventModel(event);

                    LocalDateTime oldStartDate = event.getEventStartTime();
                    LocalDateTime newStartDate = oldStartDate.plusDays(i + 1);
            
                    long duration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());

                    LocalDateTime endDate = newStartDate.plusMinutes(duration);

                    newEvent.setEventStart(newStartDate);
                    newEvent.setEventEnd(endDate);
                    calModel.addEvent(newEvent);
                }
                for (int i = 0; i < dayOfWeek; ++i) {
                    EventModel newEvent = new EventModel(event);

                    LocalDateTime oldStartDate = event.getEventStartTime();
                    LocalDateTime newStartDate = oldStartDate.plusDays((-i) - 1);
            
                    long duration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());

                    LocalDateTime endDate = newStartDate.plusMinutes(duration);

                    newEvent.setEventStart(newStartDate);
                    newEvent.setEventEnd(endDate);
                    calModel.addEvent(newEvent);
                }
            } else {
                LocalDateTime eventStart = event.getEventStartTime();
                int startDay = eventStart.getDayOfMonth();
                int lastDayOfMonth = LocalDate.of(eventStart.getYear(), eventStart.getMonthValue() + 1 > 12 ? 1 : eventStart.getMonthValue() + 1, 1).minusDays(1).getDayOfMonth();

                int j = startDay + 7;
                int i = 7;

                while (j <= lastDayOfMonth) {
                    EventModel newEvent = new EventModel(event);
                    LocalDateTime newEventStart = eventStart.plusDays(i);

                    long duration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());

                    LocalDateTime endTime = newEventStart.plusMinutes(duration);

                    newEvent.setEventStart(newEventStart);
                    newEvent.setEventEnd(endTime);

                    calModel.addEvent(newEvent);

                    j += 7;
                    i += 7;
                }

                j = eventStart.getDayOfMonth() - 7;
                i = 7;

                while (j > 0) {
                    EventModel newEvent = new EventModel(event);
                    LocalDateTime newEventStart = eventStart.plusDays(-i);

                    long duration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());

                    LocalDateTime endTime = newEventStart.plusMinutes(duration);

                    newEvent.setEventStart(newEventStart);
                    newEvent.setEventEnd(endTime);

                    calModel.addEvent(newEvent);

                    j -= 7;
                    i += 7;
                }
            }

        } else {
            executeSimilarMonthCommands();
        }
    }

    public void executeCommand(MonthComponentView compView) {
        if (name.equals("circle")) {
            ArrayList<EventModel> events = new ArrayList<>();
            events.addAll(calModel.getDateEvents(compView.getCompModel().getDate()));

            for (EventModel e : events) { 
                calModel.removeEvent(e);
            }
        } else {
            executeSimilarMonthCommands();
        }
    }

    private void executeSimilarMonthCommands() { 
        if (name.equals("right square bracket")) {
            goToNextDay();

        } else if (name.equals("left square bracket")) {
            goToPreviousDay();
        }
    }

    public void executeCommand(DayView dayView) {
        
    }

    public void executeCommand(EventDayView eventView) {
        EventModel event = eventView.getEvent();
        if (name.equals("caret") || name.equals("v")) {
            int adjustHour = name.equals("caret") ? -1 : 1;
            EventModel newEvent = new EventModel(event);

            LocalDateTime oldStartTime = event.getEventStartTime();
            LocalDateTime newStartTime = oldStartTime.plusHours(adjustHour);            
            long duration = ChronoUnit.MINUTES.between(event.getEventStartTime(), event.getEventEndTime());

            LocalDateTime endTime = newStartTime.plusMinutes(duration);

            newEvent.setEventStart(newStartTime);
            newEvent.setEventEnd(endTime);
            calModel.removeEvent(event);
            calModel.addEvent(newEvent);
            eventView.repaint();
            calModel.getDayView().repaint();

        } else if (name.equals("delete")) {
            calModel.removeEvent(event);
        } else if (name.equals("star")) {
            event.setTag(EventModel.Tag.FAMILY, !event.getTagStatus(EventModel.Tag.FAMILY));
        } else if (name.equals("check")) {
            event.setTag(EventModel.Tag.VACATION, !event.getTagStatus(EventModel.Tag.VACATION));
        } else if (name.equals("pigtail")) {
            event.setTag(EventModel.Tag.WORK, !event.getTagStatus(EventModel.Tag.WORK));
        } else if (name.equals("x")) {
            event.setTag(EventModel.Tag.SCHOOL, !event.getTagStatus(EventModel.Tag.SCHOOL));
        } else {
            executeSimilarDayCommands();
        }
    }

    public void executeSimilarDayCommands() {
        if (name.equals("circle")) {
            ArrayList<EventModel> events = new ArrayList<>();
            events.addAll(calModel.getCurrDateEvents());

            for (EventModel e : events) { 
                calModel.removeEvent(e);
            }
        } else if (name.equals("right square bracket")) {
            goToNextDay();
        } else if (name.equals("left square bracket")) {
            goToPreviousDay();
        }
    }

    private void goToNextDay() {
        if (!calModel.getAnimationMode()) {
            calModel.setBottomLabel("Next button clicked");
            if (calModel.getDayViewDisplayed()) {
                calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
            } else {
                calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
            }
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
            calModel.getMonthView().refreshEvents();
            fowardButtonAnimation();
            calModel.getAnimation().repaint();
        }
    }

    private void goToPreviousDay() {
        if (!calModel.getAnimationMode()) {
            calModel.setBottomLabel("Previous button clicked");
            if (calModel.getDayViewDisplayed()) {
                calModel.setCurrDate(calModel.getCurrDate().minusDays(1));
            } else {
                calModel.setCurrDate(calModel.getCurrDate().minusMonths(1));
            }
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
            calModel.getMonthView().refreshEvents();
    
            backButtonAnimation();
            calModel.getAnimation().repaint();
        }
    }

    public void fowardButtonAnimation() {
        calModel.setAnimationMode(true);
        BufferedImage frontImage;
        BufferedImage backImage;
        if (calModel.getDayViewDisplayed()) {
            
            calModel.setCurrDate(calModel.getCurrDate().plusDays(-1));
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
            frontImage = makeOffScreenImage(calModel.getDayView());
            
            
            calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());

            backImage = makeOffScreenImage(calModel.getDayView());

            calModel.getMonthView().refreshEvents();
        } else {
            
            calModel.setCurrDate(calModel.getCurrDate().plusMonths(-1));
            calModel.getMonthView().refreshEvents();
            frontImage = makeOffScreenImage(calModel.getMonthView());
            

            calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
            calModel.getMonthView().refreshEvents();
            backImage = makeOffScreenImage(calModel.getMonthView());

        }

        calModel.getAnimation().setFrontImage(frontImage);
        calModel.getAnimation().setBackImage(backImage);
        


        calModel.getAnimation().startAnimation(false, true);
        setUpAnimation();
    }

    public void backButtonAnimation() {
        calModel.setAnimationMode(true);
        BufferedImage frontImage;
        BufferedImage backImage;
        if (calModel.getDayViewDisplayed()) {
            
            frontImage = makeOffScreenImage(calModel.getDayView());
            
            
            calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());

            backImage = makeOffScreenImage(calModel.getDayView());

            
            calModel.setCurrDate(calModel.getCurrDate().plusDays(-1));
            calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
            calModel.getMonthView().refreshEvents();
        } else {
            
            
            frontImage = makeOffScreenImage(calModel.getMonthView());
            

            calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
            calModel.getMonthView().refreshEvents();
            backImage = makeOffScreenImage(calModel.getMonthView());
            calModel.setCurrDate(calModel.getCurrDate().plusMonths(-1));
            calModel.getMonthView().refreshEvents();
        }

        calModel.getAnimation().setFrontImage(frontImage);
        calModel.getAnimation().setBackImage(backImage);
        


        calModel.getAnimation().startAnimation(false, false);
        setUpAnimation();
    }

    

    private void setUpAnimation() {
        //calModel.getMonthView().getPanel().add(calModel.getAnimation());
        if (calModel.getDayViewDisplayed()) {
            if (!calModel.getDayView().getAnimationAdded()) {
                calModel.getDayView().add(calModel.getAnimation(), 0, 0);
                calModel.getDayView().setAnimationAdded(true);
            }
        } else {
            if (!calModel.getMonthView().getAnimationAdded()) {
                calModel.getMonthView().add(calModel.getAnimation(), 0, 0);
                calModel.getMonthView().setAnimationAdded(true);
            }
        }
        calModel.getAnimation().repaint();
        //calModel.getMonthView().getPanel().repaint();
        JScrollBar vertical = calModel.getMonthView().getScrollPane().getVerticalScrollBar();
        vertical.setValue(vertical.getValue() + 1);
        vertical.setValue(vertical.getValue() - 1);

    }


    public BufferedImage makeOffScreenImage(JComponent source) {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());

        Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();

        source.paint(offscreenGraphics);

        return offscreenImage;
    }
    
    public void setGlassPane(GlassPaneMonth glassPane) { this.glassPane = glassPane; }
}

