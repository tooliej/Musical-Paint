package model;

import controller.Controller;
import view.ViewDrawingBoard;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;

public class DrawingBoard extends JComponent{

    private static final int UPDATE_INTERVAL = 50; // milliseconds
    private static final int NUMBER_OF_SCREENS = 16;
    private ArrayList<Drawing> shapes = new ArrayList<Drawing>();
    private Point drawStart;
    private Point drawEnd;
    private int cursorX;
    private Graphics2D graphicSettings;
    private int currentAction = Controller.ELLIPSE;
  //  private float transparencyVal = 1.0f;
    private DrawingFunctions myFunctions = new DrawingFunctions();
    private boolean continueThread = false;
    private boolean drawLines = true; // Vertical grid

    public void paint(Graphics g){

        // Class used to define the shapes to be drawn
        graphicSettings = (Graphics2D)g;

        // Antialiasing cleans up the jagged lines
        graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawShapes();

        // Guide shape used for drawing
        if (drawStart != null && drawEnd != null){

            // Makes the guide shape transparent
            graphicSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.40f));

            // Make guide shape gray for professional look
            graphicSettings.setPaint(Color.LIGHT_GRAY);

            Shape aShape = null;

            if (currentAction == Controller.FIXED_BLUE_RECTANGLE ||
                    currentAction == Controller.FIXED_GREEN_RECTANGLE) {
                aShape = myFunctions.drawRectangle(drawStart.x, drawStart.y,
                        drawEnd.x, drawEnd.y);
            }
            else if (currentAction == Controller.ELLIPSE) {
                aShape = myFunctions.drawEllipse(drawStart.x, drawStart.y,
                        drawEnd.x, drawEnd.y);
            }
            else if (currentAction == Controller.LINE) {
                aShape = myFunctions.drawLine(drawStart.x, drawStart.y,
                        drawEnd.x, drawEnd.y);
            }
            else if (currentAction == Controller.RECTANGLE) {
                aShape = myFunctions.drawRectangle(drawStart.x, drawStart.y,
                        drawEnd.x, drawEnd.y);
            }
            else if (currentAction == Controller.TRIANGLE) {
                aShape = myFunctions.drawTriangle(drawStart.x, drawStart.y,
                        drawEnd.x, drawEnd.y);
            }

            graphicSettings.draw(aShape);
            drawShapes();
        }
    }

    public void startMusicCursor(int loops, int bpm) {
        cursorX = 0;
        continueThread = true;
        Thread updateThread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < loops && continueThread) {
                    cursorX += bpm / 3.7f; // Need to calculate exact number
                    if(cursorX > ViewDrawingBoard.CANVAS_WIDTH) {
                        i++;
                        cursorX = 0;
                    }
                    repaint();
                    try {
                        // Delay and give other thread a chance to run
                        Thread.sleep(UPDATE_INTERVAL);  // milliseconds
                    } catch (InterruptedException ignore) {}
                }

                if(!continueThread) {
                    cursorX = 0;
                    repaint();
                }
            }
        };

        updateThread.start();

    }

    public void stopCursor() { continueThread = false; }
    public Point getDrawStart() {
        return drawStart;
    }
    public void setDrawStart(Point drawStart) {
        this.drawStart = drawStart;
    }
    public void setDrawEnd(Point drawEnd) {
        this.drawEnd = drawEnd;
    }
    public ArrayList<Drawing> getShapes() {
            return shapes;
    }
    public int getCurrentAction() {
        return currentAction;
    }
    public void setCurrentAction(int currentAction) {
        this.currentAction = currentAction;
    }
    public void addMyMouseActivity(MouseAdapter mouse){
        this.addMouseListener(mouse);
    }
    public Graphics2D getGraphicSettings() { return graphicSettings; }
    public void setDrawLines(boolean bool) { drawLines = bool; }
    public boolean getDrawLines() { return drawLines; }


    private void drawShapes() {
        graphicSettings.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f));
        graphicSettings.setPaint(Color.black);
        graphicSettings.setStroke(new BasicStroke(0.5f));

        if (drawLines) {
            //Draws vertical lines as grid along the canvas
            for (int i = 0; i < NUMBER_OF_SCREENS; i++) {
                Shape line = myFunctions.drawLine((ViewDrawingBoard.CANVAS_WIDTH / NUMBER_OF_SCREENS) * i, 0,
                        (ViewDrawingBoard.CANVAS_WIDTH / NUMBER_OF_SCREENS) * i, ViewDrawingBoard.CANVAS_HEIGHT);
                graphicSettings.draw(line);
            }
        }

        graphicSettings.setStroke(new BasicStroke(1.0f));

        //Draws moving musical cursor
        Shape line = myFunctions.drawLine(cursorX, 0, cursorX, ViewDrawingBoard.CANVAS_HEIGHT);
        graphicSettings.draw(line);

        for(Drawing drawing : shapes) {
            graphicSettings.setStroke(new BasicStroke(4));

            //Sets the shapes transparency value
            graphicSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, drawing.getTransPercent()));

            graphicSettings.setPaint(drawing.getShapeColor());
            graphicSettings.draw(drawing.getShape());
            graphicSettings.fill(drawing.getShape());
        }
    }
}