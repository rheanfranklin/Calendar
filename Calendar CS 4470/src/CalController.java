package main;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A class that manages user interaction with the calendar application
 * outside ViewDay/ViewMonth and communicates to CalModel to update
 *
 */

public class CalController {
    /**
     * The view class variable that draws the calendar application
     */
    private final CalView calView;

    /**
     * The model class variable that holds all the data the view variable is based off of
     */
    private CalModel calModel;


    public CalController(CalView calView) {
        this.calView = calView;
    }

    /**
     * Handles the user interaction of clicking to switch to day view
     */
    public void clickedOnDayButton() {
        calModel.setBottomLabel("Day view selected");
        calModel.setDayViewDisplayed(true);
        calView.setUpDayView();
    }

    /**
     * Handles the user interaction of clicking to switch to month view
     */
    public void clickedOnMonthButton() {
        calModel.setBottomLabel("Month view selected");
        calModel.setDayViewDisplayed(false);
        calView.setUpMonthView();

    }

    /**
     * Handles the user interaction of clicking on the "Next" button
     */
    public void clickedOnNextButton() {
        if (!calModel.getAnimationMode()) {
        calModel.setBottomLabel("Next button clicked");
        if (calModel.getDayViewDisplayed()) {
            calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
        } else {
            calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
        }
        calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
        calModel.getMonthView().refreshEvents();
        calView.fowardButtonAnimation();
        calModel.getAnimation().repaint();
    }
    }

    /**
     * Handles the user interaction of clicking on the "Previous"" button
     */
    public void clickedOnPreviousButton() {
        if (!calModel.getAnimationMode()) {
        calModel.setBottomLabel("Previous button clicked");
        if (calModel.getDayViewDisplayed()) {
            calModel.setCurrDate(calModel.getCurrDate().minusDays(1));
        } else {
            calModel.setCurrDate(calModel.getCurrDate().minusMonths(1));
        }
        calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
        calModel.getMonthView().refreshEvents();

        calView.backButtonAnimation();
        calModel.getAnimation().repaint();
    }
    }

    /**
     * Handles the user interaction of clicking on the "Today" button
     */
    public void clickedOnTodayButton() {
        calModel.setBottomLabel("Today button clicked");
        calModel.setCurrDate(LocalDate.now());
        calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
        calModel.getMonthView().refreshEvents();
    }

    /**
     * Handles the user interaction of clicking on the "Appointment" button
     */
    public void clickedOnAppointmentButton() {
        LocalDate currDate = calModel.getCurrDate();
        LocalDateTime eventStart = LocalDateTime.of(currDate.getYear(), currDate.getMonthValue(), currDate.getDayOfMonth(), 0, 1);
        LocalDateTime eventEnd = eventStart.plusMinutes(60);

        DialogEventEdit dialogEventEdit = new DialogEventEdit(calModel, new EventModel(eventStart, eventEnd));
        dialogEventEdit.setVisible(true);
        calModel.setBottomLabel("Appointment button clicked");
    }

    public void setCalendar(CalModel calModel) {
        this.calModel = calModel;
    }

    public CalModel getCalModel() { return calModel; }

}
