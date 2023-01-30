package main;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * A helper class for the event edit dialog
 * Converts the user's start and end time selection
 * to LocalDateTime objects that can be handed to the event
 */
public class DialogEventEditHelper {
    private int startDay;
    private int startMonth;
    private int startYear;
    private int startHour;
    private int startMinute;
    private boolean isStartAM;
    private int endDay;
    private int endMonth;
    private int endYear;
    private int endHour;
    private int endMinute;
    private boolean isEndAM;

    public LocalDateTime returnStartTime() {
        if (!isStartAM) {
            if (startHour != 12) {
                startHour += 12;
            }
        } else if (startHour == 12) {
            startHour = 0;
        }
        return LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
    }

    public LocalDateTime returnEndTime() {
        if (!isEndAM) {
            if (endHour != 12) {
                endHour += 12;
            }
        }
        return  LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
    }


    public void setStartDay(int startDay) { this.startDay = startDay;}

    public void setStartMonth(int startMonth) {this.startMonth = startMonth;}

    public void setStartYear(int startYear) { this.startYear = startYear; }

    public void setStartHour(int startHour) { this.startHour = startHour; }

    public void setStartMinute(int startMinute) { this.startMinute = startMinute; }

    public void setIsStartAM(boolean isStartAM) { this.isStartAM = isStartAM; }

    public void setEndDay(int endDay) { this.endDay = endDay; }

    public void setEndMonth(int endMonth) { this.endMonth = endMonth; }

    public void setEndYear(int endYear) { this.endYear = endYear; }

    public void setEndHour(int endHour) { this.endHour = endHour; }

    public void setEndMinute(int endMinute) { this.endMinute = endMinute; }

    public void setEndAm(boolean isEndAM) { this.isEndAM = isEndAM; }


    public void setColor(int colorIdx, EventModel eventModel) {
        Color color;
        if (colorIdx == 0) {
            color = EventDayView.BLUE;
        } else if (colorIdx == 1) {
            color = EventDayView.PURPLE;
        } else if (colorIdx == 2) {
            color = EventDayView.RED;
        } else if (colorIdx == 3) {
            color = EventDayView.YELLOW;
        } else if (colorIdx == 4) {
            color = EventDayView.GREEN;
        } else {
            color = EventDayView.ORANGE;
        }
        eventModel.setColor(color);
    }

}

