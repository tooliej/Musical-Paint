package model;

public class SoundCharacteristic implements Comparable<SoundCharacteristic> {
    public static final double  RHYTHM_VALUE_RATIO = 1 / 0.9;

    private double startTime;
    private int instrument;
    private int note;
    private double rhythmValue;
    private double duration;
    private int amp;
    private SoundCharacteristic prevAdded = null;

    public SoundCharacteristic(double startTime, int instrument, int note, double duration, int amp)
    {
        this.startTime = startTime;
        this.instrument = instrument;
        this.note = note;
        this.duration = duration;
        this.rhythmValue = RHYTHM_VALUE_RATIO * duration;
        this.amp = amp;
    }

    public double getStartTime() {
        return startTime;
    }
    public int getInstrument() {
        return instrument;
    }
    public int getNote() {
        return note;
    }
    public double getDuration() {
        return duration;
    }
    public double getRhythmValue() {
        return rhythmValue;
    }
    public int getAmp() {
        return amp;
    }

    public SoundCharacteristic getPrevAdded(){
        return prevAdded; }

    public void setPrevAdded(SoundCharacteristic prevAdded){
        this.prevAdded = prevAdded; }

    @Override
    public int compareTo(SoundCharacteristic sound) {
        if(this.startTime == sound.getStartTime() && this.getNote() == sound.getNote()
                && this.duration == sound.duration) {
            return 0;
        }

        return this.startTime < sound.getStartTime()? -1 : 1;
    }
}
