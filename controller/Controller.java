package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Drawing;
import model.DrawingBoard;
import model.DrawingFunctions;
import model.ParseDrawing;
import view.ViewDrawingBoard;

public class Controller {

    //Define different shapes for each instrument
    public static final int FIXED_ELLIPSE = ParseDrawing.HIHAT;
    public static final int FIXED_BLUE_RECTANGLE = ParseDrawing.KICKDRUM;
    public static final int FIXED_GREEN_RECTANGLE = ParseDrawing.SNARE;
    public static final int ELLIPSE = ParseDrawing.STRINGS;
    public static final int LINE = ParseDrawing.EPIANO;
    public static final int RECTANGLE = ParseDrawing.SBASS;
    public static final int TRIANGLE = ParseDrawing.SAX;

    private DrawingBoard drawB = new DrawingBoard();
    private ViewDrawingBoard viewDrawB = new ViewDrawingBoard(drawB);
    private ParseDrawing playMusic = new ParseDrawing();
    private Color shapeColor = Color.BLACK;
    private float transparentVal = 1.0f;

    public Controller(){
        DrawingFunctions myFunctions = new DrawingFunctions();
        viewDrawB.addMyChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (arg0.getSource() == viewDrawB.getTransparencySlider()){
                    viewDrawB.setTransparencyLabel("Volume: " + viewDrawB.getTransparencySlider().getValue() + "%");
                    transparentVal = (float)(viewDrawB.getTransparencySlider().getValue() * .01);
                }
            }
        });

        //Buttons event listeners
        viewDrawB.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == viewDrawB.getBtnBassDrum()){
                    drawB.setCurrentAction(FIXED_BLUE_RECTANGLE);
                    //shapeColor = Color.BLUE;
                }

                else if (e.getSource() == viewDrawB.getBtnSnare()){
                    drawB.setCurrentAction(FIXED_GREEN_RECTANGLE);
                    //shapeColor = Color.GREEN;
                }

                else if (e.getSource() == viewDrawB.getBtnBass()){
                    drawB.setCurrentAction(RECTANGLE);
                    //shapeColor = Color.PINK;
                }

                else if (e.getSource() == viewDrawB.getBtnPiano()){
                    drawB.setCurrentAction(LINE);
                   // shapeColor = Color.GRAY;
                }

                else if (e.getSource() == viewDrawB.getBtnStrings()){
                    drawB.setCurrentAction(ELLIPSE);
                    //shapeColor = Color.YELLOW;
                }

                else if (e.getSource() == viewDrawB.getBtnHat()){
                    drawB.setCurrentAction(FIXED_ELLIPSE);
                    //shapeColor = Color.CYAN;
                }

                else if (e.getSource() == viewDrawB.getBtnSax()){
                    drawB.setCurrentAction(TRIANGLE);
                    //shapeColor = Color.CYAN;
                }

                else if(e.getSource() == viewDrawB.getBtnRefresh()){
                    drawB.getShapes().clear();
                    playMusic.clear();
                    drawB.repaint();
                }

                else if(e.getSource() == viewDrawB.getBtnUndo()){
                    int size = drawB.getShapes().size();
                    if(size > 1) {
                        playMusic.removeLast();
                        drawB.getShapes().remove(size - 1);
                    }
                    else {
                        drawB.getShapes().clear();
                        playMusic.clear();
                    }

                    drawB.repaint();
                }

                else if(e.getSource() == viewDrawB.getBtnGrid()) {
                    if(drawB.getDrawLines()) {
                        drawB.setDrawLines(false);
                        viewDrawB.getBtnGrid().setText("Add Grid");
                        drawB.repaint();
                    }
                    else {
                        drawB.setDrawLines(true);
                        viewDrawB.getBtnGrid().setText("Remove Grid");
                        drawB.repaint();
                    }
                }

                else if(e.getSource() == viewDrawB.getBtnColor()){
                    shapeColor = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
                }

                else if(e.getSource() == viewDrawB.getBtnSave()){   // Current save does not write to intended path
                    JFileChooser chooser = new JFileChooser();
                    int returnVal = chooser.showSaveDialog(viewDrawB);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        Path path = chooser.getSelectedFile().toPath();
                        Pattern pattern = Pattern.compile("(.*/)(.*)");
                        Matcher matcher = pattern.matcher(path.toString());
                        matcher.find();
                        //String relativePath = matcher.group(1);
                        String fileName = matcher.group(2);
                        playMusic.save(fileName);

                        myFunctions.saveImage(matcher.group(0) + ".png", drawB.getGraphicSettings(), drawB);
                    }
                }

                else if (e.getSource() == viewDrawB.getBtnPlay()){
                    try {
                        int loops = Integer.valueOf(viewDrawB.getLoopNum().getText());
                        int bpm = Integer.valueOf(viewDrawB.getBpmNum().getText());
                        if (loops > 0 && bpm > 0) {

                            if(!drawB.getShapes().isEmpty() && !playMusic.isPlaying()) {
                                playMusic.play(bpm, loops);
                                drawB.startMusicCursor(loops, bpm);
                            }
                        }
                        else {
                            viewDrawB.viewErrorMessage();
                        }
                    }
                    catch (NumberFormatException exception) {
                        viewDrawB.viewErrorMessage();
                    }
                }

                else if (e.getSource() == viewDrawB.getBtnPause()){
                    playMusic.pause();
                }

                else if (e.getSource() == viewDrawB.getBtnStop()){
                    if(playMusic.isPlaying()) {
                        drawB.stopCursor();
                        playMusic.stop();
                    }
                }
            }
        });

        //Mouse event listeners
        drawB.addMyMouseActivity(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                Shape aShape = null;

                //Immediately draws fixed size shapes
                if(drawB.getCurrentAction() == FIXED_ELLIPSE) {
                    aShape = myFunctions.drawFixedEllipse(e.getX(),e.getY(),10,30);
                    Drawing figure = new Drawing(aShape, shapeColor,
                            drawB.getCurrentAction(), transparentVal);
                    drawB.getShapes().add(figure);
                    playMusic.addFigure(figure);
                    drawB.setDrawStart(null);
                    drawB.setDrawEnd(null);
                }

                else if(drawB.getCurrentAction() == FIXED_BLUE_RECTANGLE ||
                        drawB.getCurrentAction() == FIXED_GREEN_RECTANGLE) {

                    if (drawB.getCurrentAction() == FIXED_BLUE_RECTANGLE) {
                        aShape = myFunctions.drawFixedRectangle(e.getX(), e.getY(), 7, 40);
                    }
                    else {
                        aShape = myFunctions.drawFixedRectangle(e.getX(), e.getY(), 15, 15);
                    }

                    Drawing figure = new Drawing(aShape, shapeColor,
                            drawB.getCurrentAction(), transparentVal);
                    drawB.getShapes().add(figure);
                    playMusic.addFigure(figure);
                    drawB.setDrawStart(null);
                    drawB.setDrawEnd(null);
                }

                else {
                    drawB.setDrawStart(new Point(e.getX(), e.getY()));
                    drawB.setDrawEnd(drawB.getDrawStart());
                }

                drawB.repaint();
            }


            public void mouseReleased(MouseEvent e) {
                Shape aShape = null;
                Point releasePoint = new Point(e.getX(), e.getY());

                if (drawB.getCurrentAction() != FIXED_ELLIPSE &&
                        drawB.getCurrentAction() != FIXED_BLUE_RECTANGLE &&
                        drawB.getCurrentAction() != FIXED_GREEN_RECTANGLE) {

                    //Assigns minimum size to shapes
                    if (releasePoint.equals(drawB.getDrawStart())) {
                        releasePoint.setLocation(releasePoint.getX() + 5, releasePoint.getY());
                    }

                    if (drawB.getCurrentAction() == LINE) {
                        aShape = myFunctions.drawLine(drawB.getDrawStart().x,
                                drawB.getDrawStart().y, releasePoint.x, releasePoint.y);
                    } else if (drawB.getCurrentAction() == ELLIPSE) {
                        aShape = myFunctions.drawEllipse(drawB.getDrawStart().x,
                                drawB.getDrawStart().y, releasePoint.x, releasePoint.y);
                    } else if (drawB.getCurrentAction() == RECTANGLE) {
                        aShape = myFunctions.drawRectangle(drawB.getDrawStart().x,
                                drawB.getDrawStart().y, releasePoint.x, releasePoint.y);
                    } else if (drawB.getCurrentAction() == TRIANGLE) {
                        aShape = myFunctions.drawTriangle(drawB.getDrawStart().x,
                                drawB.getDrawStart().y, releasePoint.x, releasePoint.y);
                    }


                    Drawing figure = new Drawing(aShape, shapeColor,
                            drawB.getCurrentAction(), transparentVal);
                    drawB.getShapes().add(figure);
                    playMusic.addFigure(figure);
                    drawB.setDrawStart(null);
                    drawB.setDrawEnd(null);
                    drawB.repaint();
                }
            }
        });
    }

    public static void main (String[] args){
        Controller c = new Controller();
        c.viewDrawB.setVisible(true);
    }
}