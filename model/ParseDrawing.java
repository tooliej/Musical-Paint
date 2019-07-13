package model;

import jm.constants.ProgramChanges;
import view.ViewDrawingBoard;

import static jm.constants.Pitches.*;

public class ParseDrawing {

    public static final int HIHAT = 1;
    public static final int KICKDRUM = 2;
    public static final int SNARE = 3;
    public static final int SBASS = ProgramChanges.SBASS;
    public static final int EPIANO = ProgramChanges.EPIANO;
    public static final int STRINGS = ProgramChanges.STRINGS;
    public static final int SAX = ProgramChanges.SOPRANO_SAX;
    private static final int QUANTIZE_PARTS = 16;
    private static final int QUATER_CANVAS = ViewDrawingBoard.CANVAS_WIDTH /4;

    private MusicSequencer music = new MusicSequencer();

    public void addFigure(Drawing figure) {
        SoundCharacteristic sound;
        int note;
        int instrument = figure.getShapeType();
        double startTime = quantize(getStartTime(figure), QUANTIZE_PARTS) / QUATER_CANVAS;
        double duration = ((figure.getShape().getBounds2D().getMaxX() -
                figure.getShape().getBounds2D().getMinX()) / QUATER_CANVAS);
        int amp = parseAmp(figure);

        switch(instrument){
            case HIHAT:
                note = FS2;
                break;
            case KICKDRUM:
                note = C2;
                break;
            case SNARE:
                note = D2;
                break;
            case SBASS:
                note = (getPentatonicNote(figure.getShape().getBounds2D().getCenterY()));
                amp *= 2; //Bass sound needs to be amplified to be heard
                break;
            default:
                note = (getPentatonicNote(figure.getShape().getBounds2D().getCenterY()));
                break;
        }
        sound = new SoundCharacteristic(startTime, instrument, note, duration, amp);
        music.addSound(sound);
    }

    public void play(int bpm, int loops){
        music.setNumLoops(loops);
        music.setBpm(bpm);
        music.play();
    }

    public boolean isPlaying() {
        return music.isPlaying();
    }
    public void pause() {
        music.pause();
    }
    public void stop() {
        music.stop();
    }
    public void save(String fileName) {
        music.save(fileName);
    }
    public void removeLast() {
        music.removeLastSound();
    }
    public void clear() {
        music.clear();
    }
    private double getStartTime(Drawing figure) {
        return (figure.getShape().getBounds2D().getX());
    }
    private int parseAmp(Drawing figure) {
        return (int)(figure.getTransPercent() * 75);
    }

    private double quantize(double start, int parts) {
        double fixedStart = start;

        if(parts > 0) {
            int accuracy = ViewDrawingBoard.CANVAS_WIDTH / parts;
            int low = 0;
            int high = parts;
            int mid = 0;

            while (low + 1 < high) {
                mid = (low + high) / 2;
                if (mid * accuracy < start) {
                    low = mid;
                } else if (mid * accuracy > start) {
                    high = mid;
                } else if (mid * accuracy == start) {
                    break;
                }
            }

            if (mid * accuracy != start) {
                if (Math.abs(low * accuracy - start) < Math.abs(high * accuracy - start)) {
                    fixedStart = low * accuracy;
                } else {
                    fixedStart = high * accuracy;
                }
            }
        }

        return fixedStart;
    }

    //60,62,64,67,69,72,74,76,79,81,84,86,88,91
    //Distances 2,2,3,2,3
    private int getPentatonicNote(double yPosition) {
        int[] notes = {60,62,64,67,69,72,74,76,79,81,84,86,88,91};

        return notes[(int) (ViewDrawingBoard.CANVAS_HEIGHT - yPosition) /
                (ViewDrawingBoard.CANVAS_HEIGHT / notes.length)] / 2;
    }
}
