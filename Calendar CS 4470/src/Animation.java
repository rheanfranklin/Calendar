package main;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class Animation extends JComponent implements Scrollable {

    private static final int ANIMATION_RATE = 4;

    private final CalModel calModel;

    private BufferedImage frontImage;
    private BufferedImage backImage;
    private JScrollPane scrollPane;
    private boolean mouseReleased;
    private boolean mouseInitiated;
    private boolean forward;
    private int width;
    private MouseEvent mouseEvent;
    private int releasedX;
    private boolean changeDate;

    private Point mousePoint;
    private Timer timer;
    private int x;


    public Animation(CalModel calModel) {
        this.calModel = calModel;
        mouseReleased = false;
        changeDate = false;

        x = -1;
        width = 65;
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                performAction();
            }
        };

        timer = new Timer(1, actionListener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        if (frontImage == null || backImage == null) {
            throw new NullPointerException("Null component in animation object");
        }

        if (mouseInitiated && mousePoint == null) {
            throw new NullPointerException("Mouse point is null");
        }


        if (x > 0) {
            

            g.drawImage(backImage, 0, 0, this);
            BufferedImage frontPortion = frontImage.getSubimage(0, 0, Math.min(Math.max(x, 1), frontImage.getWidth()), frontImage.   getHeight());
            g.drawImage(frontPortion, 0, 0, this);

            g.setColor(Color.WHITE);
            g.fillRect(x, 0, width, (int)frontImage.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(x, 0, width, (int)frontImage.getHeight());
        } else if (x < 0) {
            calModel.setAnimationMode(false);
            if (calModel.getDayViewDisplayed()) {
                if (changeDate) {
                    if (forward) {
                        calModel.setCurrDate(calModel.getCurrDate().plusDays(1));
                    } else {
                        calModel.setCurrDate(calModel.getCurrDate().plusDays(-1));
                    }
                    calModel.getDayView().refreshEvents(calModel.getCurrDateEvents());
                }
                
                calModel.getDayView().remove(this);
                calModel.getDayView().setAnimationAdded(false);
                calModel.getDayView().repaint();
            } else {
                if (changeDate) {
                    if (forward) {
                        calModel.setCurrDate(calModel.getCurrDate().plusMonths(1));
                    } else {
                        calModel.setCurrDate(calModel.getCurrDate().plusMonths(-1));
                    }
                    calModel.getMonthView().refreshEvents();
                }
                calModel.getMonthView().remove(this);
                calModel.getMonthView().setAnimationAdded(false);
                calModel.getMonthView().repaint();
            }
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getValue() + 1);
            mouseReleased = false;
            changeDate = false;
            vertical.setValue(vertical.getValue() - 1);
            scrollPane.repaint();
            timer.stop();
        }   

       
        int rgb = 50;
        if (forward) {
            for (int i = 0; i < width - 1; ++i) {
                Color color = new Color(rgb, rgb, rgb);
                g.setColor(color);
                g.drawLine(x + width - i + 1, 0, x + width - i + 1, (int)frontImage.getHeight());
                if (rgb + 10 > 255) {
                    rgb = 255;
                } else {
                    rgb += 10;
                }
            }
        } else {
            for (int i = 0; i < width - 1; ++i) {
                Color color = new Color(rgb, rgb, rgb);
                g.setColor(color);
                g.drawLine(x + i + 1, 0, x + i + 1, (int)frontImage.getHeight());
                if (rgb + 10 > 255) {
                    rgb = 255;
                } else {
                    rgb += 10;
                }
            }
        }
    }

    public void startAnimation(boolean mouseInitated, boolean forward) { 
        this.mouseInitiated = mouseInitated;
        this.forward = forward;
        calModel.setAnimationMode(true);
        timer.start();
        if (forward && mouseInitated) {
            x = (int)mousePoint.getX();
            width = 65;
            
        } else if (!forward && mouseInitated) {
            x = (int)mousePoint.getX();
            width = 65;
        } else if (forward && !mouseInitated) {
            if (calModel.getDayViewDisplayed()) {
                x = DayView.DAY_VIEW_LENGTH - 65;

            } else {
                x = scrollPane.getWidth() - 65;
            }
            width = 65;
        } else if (!forward && !mouseInitated) {
            x = 1;
            width = 20;
        }
    }

    public void performAction() {
        if (forward && mouseInitiated) {
            if (mouseReleased) {
                if (calModel.getDayViewDisplayed()) {
                    if (releasedX <= (DayView.DAY_VIEW_LENGTH / 2)) {
                        if (width < 0) {
                            x = -1;
                            width = 65;
                        } else if (x == 1) {
                            width -= ANIMATION_RATE;
                        } else if (x != -1){
                            if (x - ANIMATION_RATE < 0) {
                                x = 1;
                            } else {
                                x -= ANIMATION_RATE;
                            }
                            ++width;
                        }
                        changeDate = true;
                        repaint();
                    } else {
                        if (x + width >= DayView.DAY_VIEW_LENGTH) {
                            if (width - ANIMATION_RATE < 1) {
                                width = 1;
                            } else {
                                width -= ANIMATION_RATE;
                            }
                            if (x + width + ANIMATION_RATE > DayView.DAY_VIEW_LENGTH) {
                                x = scrollPane.getWidth() - width;
                            } else {
                                x += ANIMATION_RATE;
                            }
                        } else {
                            x += ANIMATION_RATE;
                        }
                        if (width == 1) {
                            x = -1;
                        } 
                        repaint();

                    }
                } else {
                    if (releasedX <= (scrollPane.getWidth() / 2)) {
                        if (width < 0) {
                            x = -1;
                            width = 65;
                        } else if (x == 1) {
                            width -= ANIMATION_RATE;
                        } else if (x != -1){
                            if (x - ANIMATION_RATE < 0) {
                                x = 1;
                            } else {
                                x -= ANIMATION_RATE;
                            }
                            ++width;
                        }
                        changeDate = true;
                        repaint();
                    } else {
                        if (x + width >= scrollPane.getWidth()) {
                            if (width - ANIMATION_RATE < 1) {
                                width = 1;
                            } else {
                                width -= ANIMATION_RATE;
                            }
                            if (x + width + ANIMATION_RATE > scrollPane.getWidth()) {
                                x = scrollPane.getWidth() - width;
                            } else {
                                x += ANIMATION_RATE;
                            }
                        } else {
                            x += ANIMATION_RATE;
                        }
                        if (width == 1) {
                            x = -1;
                        } 
                        repaint();
                    }
                }
            }
        } else if (!forward && mouseInitiated) {
            if (mouseReleased) {
                if (calModel.getDayViewDisplayed()) {
                    if (releasedX >= (DayView.DAY_VIEW_LENGTH / 2)) {
                        if (x + width >= scrollPane.getWidth()) {
                            if (width - ANIMATION_RATE < 1) {
                                width = 1;
                            } else {
                                width -= ANIMATION_RATE;
                            }
                            if (x + width + ANIMATION_RATE > scrollPane.getWidth()) {
                                x = scrollPane.getWidth() - width;
                            } else {
                                x += ANIMATION_RATE;
                            }
                        } else {
                            x += ANIMATION_RATE;
                        }
                        if (width == 1) {
                            x = -1;
                        } 
                        changeDate = true;
                        repaint();
                    } else {

                        if (width < 0) {
                            x = -1;
                            width = 65;
                        } else if (x == 1) {
                            width -= ANIMATION_RATE;
                        } else if (x != -1){
                            if (x - ANIMATION_RATE < 0) {
                                x = 1;
                            } else {
                                x -= ANIMATION_RATE;
                            }
                            ++width;
                        }
                        repaint();
                    }

                } else {
                    if (releasedX >= (scrollPane.getWidth() / 2)) {
                        if (x + width >= scrollPane.getWidth()) {
                            if (width - ANIMATION_RATE < 1) {
                                width = 1;
                            } else {
                                width -= ANIMATION_RATE;
                            }
                            if (x + width + ANIMATION_RATE > scrollPane.getWidth()) {
                                x = scrollPane.getWidth() - width;
                            } else {
                                x += ANIMATION_RATE;
                            }
                        } else {
                            x += ANIMATION_RATE;
                        }
                        if (width == 1) {
                            x = -1;
                        } 
                        changeDate = true;
                        repaint();
                    } else {
                        if (width < 0) {
                            x = -1;
                            width = 65;
                        } else if (x == 1) {
                            width -= ANIMATION_RATE;
                        } else if (x != -1){
                            if (x - ANIMATION_RATE < 0) {
                                x = 1;
                            } else {
                                x -= ANIMATION_RATE;
                            }
                            ++width;
                        }
                        repaint();
                    }
                }
            }
        } else if (forward && !mouseInitiated) {
            if (width < 0) {
                x = -1;
                width = 65;
            } else if (x == 1) {
                width -= ANIMATION_RATE;
            } else if (x != -1){
                if (x - ANIMATION_RATE < 0) {
                    x = 1;
                } else {
                    x -= ANIMATION_RATE;
                }
                ++width;
            }
            repaint();
        } else if (!forward && !mouseInitiated) {
            System.out.println("width: " + width + ", x: " + x);
            if (calModel.getDayViewDisplayed()) {
                if (x + width >= DayView.DAY_VIEW_LENGTH) {
                    if (width - ANIMATION_RATE < 1) {
                        width = 1;
                    } else {
                        width -= ANIMATION_RATE;
                    }
                    if (x + ANIMATION_RATE + width > DayView.DAY_VIEW_LENGTH) {
                        x = DayView.DAY_VIEW_LENGTH - width;
                    } else {
                        x += ANIMATION_RATE;
                    }
                } else {
                    x += ANIMATION_RATE;
                    if (width != 1) {
                        width += ANIMATION_RATE / 2;
                    }
                }
                if (width == 1) {
                    x = -1;
                }
            } else {
                if (x + width >= scrollPane.getWidth()) {
                    if (width - ANIMATION_RATE < 1) {
                        width = 1;
                    } else {
                        width -= ANIMATION_RATE;
                    }
                    if (x + width + ANIMATION_RATE > scrollPane.getWidth()) {
                        x = scrollPane.getWidth() - width;
                    } else {
                        x += ANIMATION_RATE;
                    }
                } else {
                    x += ANIMATION_RATE;
                    if (width != 1) {
                        width += ANIMATION_RATE / 2;
                    }
                }
                if (width == 1) {
                    x = -1;
                } 

            }
            repaint();
        }
    }



    public void setFrontImage(BufferedImage frontImage) { this.frontImage = frontImage; }

    public void setBackImage(BufferedImage backImage) { this.backImage = backImage; }

    public void setMousePoint(Point mousePoint) { 
        this.mousePoint = mousePoint; 
        this.x = (int) mousePoint.getX();
    }

    public void setScrollPane(JScrollPane scrollPane) { this.scrollPane = scrollPane; }

    public void setMouseReleased(boolean mouseReleased) { 
        this.mouseReleased = mouseReleased; 
        releasedX = (int)mousePoint.getX();
        x = releasedX; 
    }
    
    public Point getMousePoint() { return mousePoint; }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(450, 800);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(scrollPane.getWidth(), scrollPane.getHeight());
    }
    
}

