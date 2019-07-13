package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import model.DrawingBoard;

public class ViewDrawingBoard extends JFrame{
    public static final int CANVAS_WIDTH = 1200;
    public static final int CANVAS_HEIGHT = 750;

    private JPanel bottomPanel;
    private JPanel topPanel;

    //Buttons
    private JButton btnBassDrum;
    private JButton btnSnare;
    private JButton btnBass;
    private JButton btnPiano;
    private JButton btnStrings;
    private JButton btnHat;
    private JButton btnSax;
    private JButton btnPlay;
    private JButton btnPause;
    private JButton btnStop;
    private JButton btnRefresh;
    private JButton btnUndo;
    private JButton btnSave;
    private JButton btnColor;
    private JButton btnGrid;

    //Icons
    private ImageIcon iconBassDrum;
    private ImageIcon iconSnare;
    private ImageIcon iconBass;
    private ImageIcon iconPiano;
    private ImageIcon iconHat;
    private ImageIcon iconSax;
    private ImageIcon iconStrings;
    private ImageIcon iconRefresh;
    private ImageIcon iconUndo;
    private ImageIcon iconSave;
    private ImageIcon iconPlay;
    private ImageIcon iconPause;
    private ImageIcon iconStop;
    private ImageIcon iconColor;

    //Bottom Panel
    private JLabel transparencyLabel;
    private JSlider transparencySlider;
    private JLabel loopLabel;
    private JTextField loopNum;
    private JLabel bpmLabel;
    private JTextField bpmNum;

    //Boxes
    private Box bottomBox;
    private Box topBox;


    public ViewDrawingBoard(DrawingBoard myDrawingBoard){
        //Set canvas boarders
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
        Border redline = BorderFactory.createLineBorder(Color.red);
        compound = BorderFactory.createCompoundBorder(
                redline, compound);
        ((JComponent) this.getContentPane()).setBorder(compound);

        setTitle("Musical Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(30, 30, CANVAS_WIDTH, CANVAS_HEIGHT);
        setResizable(false);
        getContentPane().add(myDrawingBoard, BorderLayout.CENTER);

        bottomBox = Box.createHorizontalBox();
        topBox = Box.createHorizontalBox();

        importIcons();
        createElements();
        addElements();

        bottomPanel.add(bottomBox);
        topPanel.add(topBox);
        compound = BorderFactory.createCompoundBorder(redline, compound);
        bottomPanel.setBorder(compound);
        topPanel.setBorder(compound);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    public void     viewErrorMessage() {
        JOptionPane.showMessageDialog(ViewDrawingBoard.this,
                "Input values must be positive integers", "Invalid Input",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getBtnBassDrum() {
        return btnBassDrum;
    }
    public JButton getBtnSnare() {
        return btnSnare;
    }
    public JButton getBtnBass() { return btnBass; }
    public JButton getBtnPiano() { return btnPiano; }
    public JButton getBtnHat() { return btnHat; }
    public JButton getBtnStrings() { return btnStrings; }
    public JButton getBtnSax() {
        return btnSax;
    }
    public void setTransparencyLabel(String text) {
        this.transparencyLabel.setText(text);
    }
    public JSlider getTransparencySlider() {
        return transparencySlider;
    }
    public JTextField getLoopNum() { return loopNum; }
    public JTextField getBpmNum() { return bpmNum; }
    public JButton getBtnRefresh() {
        return btnRefresh;
    }
    public JButton getBtnUndo() {
        return btnUndo;
    }
    public void addMyChangeListener(ChangeListener listener){
        transparencySlider.addChangeListener(listener);
    }
    public JButton getBtnSave() {
        return btnSave;
    }
    public JButton getBtnPlay() {
        return btnPlay;
    }
    public JButton getBtnPause() {
        return btnPause;
    }
    public JButton getBtnStop() {
        return btnStop;
    }
    public JButton getBtnColor() {
        return btnColor;
    }
    public JButton getBtnGrid() {
        return btnGrid;
    }

    public void addButtonActionListener(ActionListener listener) {
        btnBassDrum.addActionListener(listener);
        btnSnare.addActionListener(listener);
        btnBass.addActionListener(listener);
        btnPiano.addActionListener(listener);
        btnHat.addActionListener(listener);
        btnStrings.addActionListener(listener);
        btnSax.addActionListener(listener);
        btnRefresh.addActionListener(listener);
        btnUndo.addActionListener(listener);
        btnSave.addActionListener(listener);
        btnPlay.addActionListener(listener);
        btnPause.addActionListener(listener);
        btnStop.addActionListener(listener);
        btnColor.addActionListener(listener);
        btnGrid.addActionListener(listener);
    }

    private void importIcons() {
        iconBassDrum = new ImageIcon("bin/bassDrum.png");
        iconStrings= new ImageIcon("bin/orpheus.png");
        iconSnare = new ImageIcon("bin/snareDrum.png");
        iconBass = new ImageIcon("bin/bassGuitar.png");
        iconPiano = new ImageIcon("bin/electronic.png");
        iconHat = new ImageIcon("bin/hihat.png");
        iconRefresh = new ImageIcon("bin/refresh.png");
        iconUndo = new ImageIcon("bin/undo.png");
        iconSave = new ImageIcon("bin/save.png");
        iconPlay = new ImageIcon("bin/play.png");
        iconPause = new ImageIcon("bin/pause.png");
        iconStop = new ImageIcon("bin/stop.png");
        iconColor = new ImageIcon("bin/color.png");
        iconSax = new ImageIcon("bin/saxophone.png");
    }

    private void createElements() {
        bottomPanel = new JPanel();
        topPanel = new JPanel();

        btnBassDrum = new JButton(iconBassDrum);
        btnBass = new JButton(iconBass);
        btnPiano = new JButton(iconPiano);
        btnSnare = new JButton(iconSnare);
        btnHat = new JButton(iconHat);
        btnStrings = new JButton(iconStrings);
        btnSax = new JButton(iconSax);
        btnRefresh = new JButton(iconRefresh);
        btnUndo = new JButton(iconUndo);
        btnSave = new JButton(iconSave);
        btnPlay = new JButton(iconPlay);
        btnPause = new JButton(iconPause);
        btnStop = new JButton(iconStop);
        btnColor = new JButton(iconColor);
        btnGrid = new JButton("Remove Grid");

        transparencyLabel = new JLabel("Volume: 100%");
        transparencySlider = new JSlider(1, 100, 100);
        transparencySlider.setPreferredSize(new Dimension(150, 50));
        loopLabel = new JLabel("Number of loops: ");
        loopNum = new JTextField("1", 3);
        loopNum.setHorizontalAlignment(0);
        bpmLabel = new JLabel("Tempo (Speed): ");
        bpmNum = new JTextField("70", 3);
        bpmNum.setHorizontalAlignment(0);
    }

    private void addElements() {
        topBox.add(btnHat);
        topBox.add(btnBassDrum);
        topBox.add(btnSnare);
        topBox.add(btnBass);
        topBox.add(btnPiano);
        topBox.add(btnStrings);
        topBox.add(btnSax);

        bottomBox.add(btnGrid);
        bottomBox.add(bpmLabel);
        bottomBox.add(bpmNum);
        bottomBox.add(loopLabel);
        bottomBox.add(loopNum);
        bottomBox.add(btnColor);
        bottomBox.add(btnPlay);
        bottomBox.add(btnPause);
        bottomBox.add(btnStop);
        bottomBox.add(btnUndo);
        bottomBox.add(btnRefresh);
        bottomBox.add(btnSave);
        bottomBox.add(transparencyLabel);
        bottomBox.add(transparencySlider);
    }
}