package main;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class EventModel {

    public enum Tag {
        VACATION,
        WORK,
        SCHOOL,
        FAMILY
    } 

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private String description;
    private String title;
    private String location;
    private HashMap<Tag, Boolean> tags;
    private EventDayView eventDayView;
    private EventMonthView eventMonthView;
    private Color color;

    public EventModel(LocalDateTime eventStart, LocalDateTime eventEnd, String title, String description) {
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.title = title;
        this.description = description;
        location = "";
        tags = new HashMap<>();
        setTag(Tag.VACATION, false);
        setTag(Tag.FAMILY, false);
        setTag(Tag.WORK, false);
        setTag(Tag.SCHOOL, false);

    }

    public EventModel(LocalDateTime eventStart, LocalDateTime eventEnd, String title) {
        this(eventStart, eventEnd, title, "");
    }

    public EventModel(LocalDateTime eventStart, LocalDateTime eventEnd) {
        this (eventStart, eventEnd, "(No title)", "");
    }

    public EventModel(EventModel eventModel) {
        this.eventStart = eventModel.getEventStartTime();
        this.eventEnd = eventModel.getEventEndTime();
        this.description = eventModel.getDescription();
        this.title = eventModel.getTitle();
        this.location = eventModel.getLocation();
        this.eventMonthView = eventModel.getEventMonthView();
        tags = new HashMap<>();
        tags.put(Tag.VACATION, eventModel.getTagStatus(Tag.VACATION));
        tags.put(Tag.SCHOOL, eventModel.getTagStatus(Tag.SCHOOL));
        tags.put(Tag.WORK, eventModel.getTagStatus(Tag.WORK));
        tags.put(Tag.FAMILY, eventModel.getTagStatus(Tag.FAMILY));
        this.color = eventModel.getColor();
    }

    public void lowerExistingEventDragged(int currY, long startY) {

        double addMin = ((double)currY - startY) * ((double)60 / (double)DayView.LINE_SPACING);



        LocalDateTime eventStart = getEventStartTime();
        eventStart = eventStart.plusMinutes((long)addMin);

        long eventDuration = ChronoUnit.MINUTES.between(getEventStartTime(), getEventEndTime());

        LocalDateTime eventEnd = eventStart.plusMinutes(eventDuration);

        setEventStart(eventStart);
        setEventEnd(eventEnd);

        //LocalDateTime newEventStart = eventModel.getEventStartTime().plusMinutes((int)addMinutes);

        //if (newEventStart.getMinute() <= 7) {
        //    newEventStart = newEventStart.minusMinutes(newEventStart.getMinute());
        //} else if (newEventStart.getMinute() <= 22) {
        //    newEventStart = newEventStart.minusMinutes(newEventStart.getMinute());
        //    newEventStart = newEventStart.plusMinutes(15);
        //} else if (newEventStart.getMinute() <= 37) {
        //    newEventStart = newEventStart.minusMinutes(newEventStart.getMinute());
        //    newEventStart = newEventStart.plusMinutes(30);
        //} else if (newEventStart.getMinute() <= 44) {
        //    newEventStart = newEventStart.minusMinutes(newEventStart.getMinute());
        //    newEventStart = newEventStart.plusMinutes(45);
        //} else {
        //    newEventStart = newEventStart.plusHours(1);
        //    newEventStart = newEventStart.minusMinutes(newEventStart.getMinute());
        //}



        //LocalDateTime newEventEnd = newEventStart.plusMinutes((int)eventDuration);

        //System.out.println(newEventStart);
        //eventModel.setEventStart(newEventStart);
        //eventModel.setEventEnd(newEventEnd);
    }

    public void raiseExistingEventDragged(int prevY, long currY) {
        double minusMin = ((double)prevY - currY) * ((double)60 / (double)DayView.LINE_SPACING);

        LocalDateTime eventStart = getEventStartTime();
        eventStart = eventStart.minusMinutes((long)minusMin);

        long eventDuration = ChronoUnit.MINUTES.between(getEventStartTime(), getEventEndTime());

        LocalDateTime eventEnd = eventStart.plusMinutes(eventDuration);

        setEventStart(eventStart);
        setEventEnd(eventEnd);
    }

    public void setTag(Tag tag, Boolean tagStatus) {
        tags.put(tag, tagStatus);
    }

    public void setEventStart(LocalDateTime eventStart) {
        this.eventStart = eventStart;
    }

    public void setEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public void setEventDayView(EventDayView eventDayView) {
        if (eventDayView == null && this.eventDayView != null) {
            this.eventDayView.setVisible(false);
            this.eventDayView.setEnabled(false);
        }
        this.eventDayView = eventDayView;
    }

    public void setColor(Color color) { this.color = color; }

    public LocalDate getEventStartDate() {
        return eventStart.toLocalDate();
    }

    public LocalDateTime getEventStartTime() {
        return eventStart;
    }

    public LocalDate getEventEndDate() {
        return eventEnd.toLocalDate();
    }

    public LocalDateTime getEventEndTime() {
        return eventEnd;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public HashMap<Tag, Boolean> getAllTags() {
        return tags;
    }

    public Boolean getTagStatus(Tag tag) {
        return tags.get(tag);
    }

    public EventDayView getEventDayView() { return eventDayView; }

    public EventMonthView getEventMonthView() { return eventMonthView; }
    public void setEventMonthView(EventMonthView eventMonthView) { this.eventMonthView = eventMonthView; }

    public Color getColor() { return color; }



}
