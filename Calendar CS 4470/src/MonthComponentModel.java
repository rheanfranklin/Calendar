package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthComponentModel {
    private final MonthModel monthModel;
    private final CalModel calModel;
    private ArrayList<EventModel> events;
    private HashMap<LocalDate, String> holidays;
    private LocalDate date;
    private boolean active;
    private boolean weekend;
    private int arrayPosition;
    private int row;
    private int col;

    public MonthComponentModel(MonthModel monthModel) {
        this.monthModel = monthModel;
        this.calModel = monthModel.getCalModel();
        events = new ArrayList<>();
        active = false;
        weekend = false;
        setUpHolidays();
    }

    public int calculateXValue(MonthComponentView compView) {
        return compView.getWidth() * col;
    }

    public int calculateYValue(MonthComponentView compView) {
        return (compView.getHeight() * row) + 70;
    }

    private void setUpHolidays() {
        holidays = new HashMap<>();
        holidays.put(LocalDate.of(0, 12, 24), "Christmas Eve");
        holidays.put(LocalDate.of(0, 12, 25), "Christmas Day");
        holidays.put(LocalDate.of(0, 1, 1), "New Year's Day");
        holidays.put(LocalDate.of(0, 12, 31), "New Year's Eve");
        holidays.put(LocalDate.of(0, 1, 18), "Martin Luther King Jr. Day");
        holidays.put(LocalDate.of(0, 5, 31), "Memorial Day");
        holidays.put(LocalDate.of(0, 7, 5), "Independence Day");
        holidays.put(LocalDate.of(0, 11, 11), "Verterans Day");
        holidays.put(LocalDate.of(0, 11, 25), "Thanksgiving");
        holidays.put(LocalDate.of(0, 2, 14), "Valentine's Day");
        holidays.put(LocalDate.of(0, 3, 17), "St Patrick's Day");
        holidays.put(LocalDate.of(0, 5, 5), "Cinco De Mayo");
        holidays.put(LocalDate.of(0, 6, 19), "Juneteenth");
        holidays.put(LocalDate.of(0, 6, 20), "Father's Day");
        holidays.put(LocalDate.of(0, 9, 6), "Labor Day");
        holidays.put(LocalDate.of(0, 10, 11), "Indigenous People's Day");
        holidays.put(LocalDate.of(0, 10, 31), "Halloween");
        holidays.put(LocalDate.of(0, 5, 9), "Mother's Day");
    }

    public String getHoliday() {
        LocalDate newDate = LocalDate.of(0, date.getMonthValue(), date.getDayOfMonth());
        return holidays.containsKey(newDate) ? holidays.get(newDate) : null;
    }

    public MonthModel getMonthModel() { return monthModel; }

    public ArrayList<EventModel> getEvents() {return events;}
    public void setEvents() { 
        events = calModel.getDateEvents(date); 
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }


    public boolean getActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean getWeekend() { return weekend; }
    public void setWeekend(boolean weekend) { this.weekend = weekend; }

    public int getArrayPosition() { return arrayPosition; }
    public void setArrayPosition(int arrayPosition) { this.arrayPosition = arrayPosition; }

    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    
}
