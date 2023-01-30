package main;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.ViewportLayout;

/**
 * A class that draws the display of the calendar application
 */

public class CalView extends JFrame {

    /**
     * The controller object that handles user interaction with the CalView class
     */
    private final CalController calController;

    /**
     * The model object that this view class is based off of
     */
    private final CalModel calModel;

    /**
     * A panel that holds all the objects displayed on the left side of CalView
     */
    private JPanel mainPanel;


    /**
     * The Scroll pane that holds the day/month view
     */
    private final JScrollPane scrollPane;

    private boolean animationMode;

    private Timer timer;

    /**
     * Class constructor
     *
     * Initializes and assigns the class's controller which handles user interaction with the calendar application
     * Initializes and assignes the calModel that the calView will be based off of
     * Sets the CalController's calModel variable
     * Calls the function to set up the window's components
     */
    public CalView() {
        calModel = new CalModel();
        calController = new CalController(this);
        calController.setCalendar(calModel);
        scrollPane = new JScrollPane();
        animationMode = false;
        calModel.getAnimation().setScrollPane(scrollPane);
        setUp();

    }

    //sets up the window objects
    public void setUp() {
        //setting up window
        setSize(700, 1050);
        setTitle("main.main.Calendar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //setting up main background panel
        //JPanel mainPanel = new JPanel();
        //mainPanel.setLayout(new GridLayout(1,2));
        

        Color background = new Color(255,255, 255);
        setBackground(background);

        //setting up left panel
        mainPanel = new JPanel();
        mainPanel.setBackground(background);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(background);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(mainPanel);

        //creating JLabel at bottom of frame
        //mainPanel.add(BorderLayout.PAGE_END, calModel.getBottomLabel());
        calModel.setBottomLabel("Hey buddy :)");

        //calling on setUpMonthDayMenuBar() to set up
        //menu & radio buttons for month/day view
        JComboBox<String> monthDayMenuBar = setUpMonthDayMenuBar();

        //creating JPanel to hold Day/Month view and direction buttons
        JPanel holdDirectionButtonsAndView =  new JPanel();
        holdDirectionButtonsAndView.setLayout(new GridLayout(3, 1));
        holdDirectionButtonsAndView.setBackground(background);

        //setting up today button & it's action listoner in the controller class
        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(e -> calController.clickedOnTodayButton());


        //Adding the monthDayMenuBar to be above the todayButton
        holdDirectionButtonsAndView.add(monthDayMenuBar);
        holdDirectionButtonsAndView.add(todayButton);

        setUpDirectionButtons(holdDirectionButtonsAndView);

        mainPanel.add(holdDirectionButtonsAndView, BorderLayout.NORTH);


        //setting up new appointment button
        JButton appointmentButton = new JButton("New Appointment");
        appointmentButton.addActionListener(e -> calController.clickedOnAppointmentButton());

        mainPanel.add( appointmentButton, BorderLayout.SOUTH);

        //adding JLabel displaying date to right panel
        setUpDayView();

        scrollPane.setViewportView(calModel.getMonthView().getPanel());



        pack();
        setUpMonthView();
        setUpDayView();
        
    }


    /**
     * Sets up the month/day option menu
     *
     * @return JMenuBar that holds the month & day buttons
     */
    private JComboBox<String> setUpMonthDayMenuBar() {
        String[] dayMonthOption = { "Day View", "Month View" };

        JComboBox<String> dayMonthComboBox = new JComboBox<>(dayMonthOption);
        dayMonthComboBox.setSelectedIndex(0);

        dayMonthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dayMonthComboBox.getSelectedIndex() == 0) {
                    calController.clickedOnDayButton();
                } else {
                    calController.clickedOnMonthButton();
                }
            }
        });

        return dayMonthComboBox;
    }


    /**
     * Sets up the next and previous buttons
     *
     * @param panel - the panel the buttons are placed on
     */
    private void setUpDirectionButtons(JPanel panel) {
        JPanel holdDirectionButtons = new JPanel();
        //holdDirectionButtons.setBackground(new Color(100,100,100));
        holdDirectionButtons.setLayout(new GridLayout(1, 2));

        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");

        holdDirectionButtons.add(previousButton);
        holdDirectionButtons.add(nextButton);

        previousButton.addActionListener(e -> calController.clickedOnPreviousButton());

        nextButton.addActionListener(e -> calController.clickedOnNextButton());

        panel.add(holdDirectionButtons);
    }

    /**
     * Sets up the dayView whenever the "Day View" option is selected
     */
    public void setUpDayView() {
        calModel.getMonthView().getPanel().removeAll();
        calModel.getMonthView().getPanel().add(calModel.getDayView());
        calModel.getMonthView().getPanel().setAlignmentX(Component.CENTER_ALIGNMENT);
        calModel.getMonthView().getPanel().repaint();
        pack();
    }

    public void setUpMonthView() {
        calModel.getMonthView().setScrollPane(scrollPane);
        calModel.getMonthView().getPanel().removeAll();
        calModel.getMonthView().getPanel().add(calModel.getMonthView());
        calModel.getMonthView().repaint();
        calModel.getMonthView().getPanel().repaint();
        
        setSize(900, 1050);

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
        animationMode = true;
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
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

    public static void main(String[] args) {
        CalView calendar = new CalView();
        calendar.setVisible(true);
    }
}






























