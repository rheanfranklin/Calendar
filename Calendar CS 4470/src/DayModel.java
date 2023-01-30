package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DayModel {

    private HashMap<Integer, Long> yValuesPerHour;

    final private CalModel calModel;

    final private GlassPaneDay glassPane;

    public DayModel(CalModel calModel) {
        yValuesPerHour = new HashMap<>();
        glassPane = new GlassPaneDay(calModel);
        this.calModel = calModel;
    }

    public EventModel createEventFromMousePressToDrag(int startY) {
        LocalDate currDate = calModel.getCurrDate();
        int eventStartHour = 0;
        int eventStartMinute = 0;

        eventStartHour = getHour(startY);
        LocalDateTime eventStart = generateEventTime(startY, currDate, eventStartHour, eventStartMinute);

        LocalDateTime eventEnd = eventStart.plusMinutes(60);

        return new EventModel(eventStart, eventEnd);
    }

    public void updatedDraggedEventEndTime(int currY, EventModel eventModel) {
        LocalDate currDate = calModel.getCurrDate();
        int eventEndHour;
        int eventEndMinute = 0;

        eventEndHour = getHour(currY);

        LocalDateTime eventEnd = generateEventTime(currY, currDate, eventEndHour, eventEndMinute);

        if (eventModel == null) {
            //eventModel = createEventFromMouseClick(currY, currDate);
            calModel.addEvent(eventModel);
        }
        eventModel.setEventEnd(eventEnd);

    }



    public LocalDateTime generateEventTime(int currY, LocalDate currDate, int hour, int minute) {
        if (hour != 0) {

            double pixelsPerMin = (double) DayView.LINE_SPACING / 60;

            if (currY < (getEventHourYValue(hour) + (7 * pixelsPerMin))) {
                minute = 0;
            } else if (currY < (getEventHourYValue(hour) + (22 * pixelsPerMin))) {
                minute = 15;
            } else if (currY < (getEventHourYValue(hour) + (37 * pixelsPerMin))) {
                minute = 30;
            } else if (currY < (getEventHourYValue(hour) + (52 * pixelsPerMin))) {
                minute = 45;
            } else {
                ++hour;
                minute = 0;
            }
        }

        return LocalDateTime.of(currDate.getYear(), currDate.getMonthValue(),
                currDate.getDayOfMonth(), hour, minute);
    }

    public int getHour(long y) {
        int hour = 0;
        for(int i = 0; i < 23; ++i) {
            if (getEventHourYValue(i) < y && getEventHourYValue(i + 1) > y) {
                hour = i;
            }
        }
        return hour;
    }

    public static int getEventHourYValue(int hour) {
        return DayView.EVENT_START_Y_VALUE + (DayView.LINE_SPACING * hour);
    }

    public static int getEventStartYValue(EventModel event) {
        LocalDateTime eventStart = event.getEventStartTime();

        int eventYValue = getEventHourYValue(eventStart.getHour());

        int minutes = eventStart.getMinute();
        double pixelsPerMin = (double)DayView.LINE_SPACING / 60;

        eventYValue += ((double)minutes * pixelsPerMin);

        return eventYValue;
    }

    //setter for yValuesPerHour
    public long getYValuePerHour(Integer hour) { return yValuesPerHour.get(hour); }

    public void putYValuesPerHour(Integer hour, long yValue) { yValuesPerHour.put(hour, yValue); }

    //getter for calModel

    public CalModel getCalModel() { return calModel; }

    public LocalDate getCurrDate() { return calModel.getCurrDate(); }

    public GlassPaneDay getGlassPane() { return glassPane; }
}
