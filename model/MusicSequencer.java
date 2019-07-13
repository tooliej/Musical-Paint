package model;

import static jm.constants.ProgramChanges.*;

import jm.midi.MidiSynth;
import jm.music.data.*;
import jm.music.tools.Mod;
import jm.util.Play;
import jm.util.Write;

import javax.sound.midi.InvalidMidiDataException;
import java.util.Iterator;
import java.util.TreeSet;

public class MusicSequencer {
    private int numLoops = 1;
    private int bpm = 70;

    private MidiSynth sequence;
    private Score music;
    private Part drums;
    private Part bass;
    private Part piano;
    private Part strings;
    private Part sax;

    private Phrase kickPhrase;
    private Phrase snarePhrase;
    private Phrase hatPhrase;
    private CPhrase bassPhrase;
    private CPhrase pianoPhrase;
    private CPhrase stringsPhrase;
    private CPhrase saxPhrase;

    private TreeSet<SoundCharacteristic> hatNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> snareNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> kickDrumNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> bassNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> pianoNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> stringsNotes = new TreeSet<>();
    private TreeSet<SoundCharacteristic> saxNotes = new TreeSet<>();

    private boolean loopChange = false;
    private boolean tempoChange = false;
    private boolean kickDrumAdded = false;
    private boolean hatAdded = false;
    private boolean snareAdded = false;
    private boolean bassAdded = false;
    private boolean pianoAdded = false;
    private boolean stringsAdded = false;
    private boolean saxAdded = false;

    private SoundCharacteristic lastAdded = null;

    public MusicSequencer(){
        sequence = new MidiSynth();
        drums = new Part("Drums", 25, 9);
        bass = new Part("Bass", SBASS, 0);
        piano = new Part("Piano", EPIANO, 1);
        strings = new Part("Strings", VIBES, 2);
        sax = new Part("Strings", SOPRANO_SAX, 3);

        kickPhrase = new Phrase(0.0);
        snarePhrase = new Phrase(0.0);
        hatPhrase = new Phrase(0.0);
        bassPhrase = new CPhrase(0.0);
        pianoPhrase = new CPhrase(0.0);
        stringsPhrase = new CPhrase(0.0);
        saxPhrase = new CPhrase(0.0);
    }

    public void setNumLoops(int loops) {
        if(loops != numLoops) {
            numLoops = loops;
            loopChange = true;
        }
    }

    public void setBpm(int bpmInput) {
        if(bpmInput != bpm) {
            bpm = bpmInput;
            loopChange = true;
        }
    }

    public void buildSequence() {
        //Sets up drum part
        setUpDrums(kickDrumAdded, kickPhrase, kickDrumNotes);
        setUpDrums(hatAdded, hatPhrase, hatNotes);
        setUpDrums(snareAdded, snarePhrase, snareNotes);

        if(kickDrumAdded || hatAdded || snareAdded) {
            drums.empty();
            drums.addPhrase(hatPhrase);
            drums.addPhrase(kickPhrase);
            drums.addPhrase(snarePhrase);
        }

        //Sets up parts
        setUpPart(bassAdded, bass, bassPhrase, bassNotes);
        setUpPart(pianoAdded, piano, pianoPhrase, pianoNotes);
        setUpPart(stringsAdded, strings, stringsPhrase, stringsNotes);
        setUpPart(saxAdded, sax, saxPhrase, saxNotes);

        //Adds parts to score
        if(loopChange || tempoChange || bassAdded || pianoAdded || stringsAdded
                || kickDrumAdded || hatAdded || snareAdded || saxAdded) {
            music = new Score(bpm);
            music.addPart(drums);
            music.addPart(bass);
            music.add(piano);
            music.add(strings);
            music.add(sax);
        }

        resetChangesMade();
    }

    public void play() {
        buildSequence();

        try {
            sequence.play(music);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //TODO Pause not implemented
        Play.pauseAudio();
    }

    public void stop() {
        sequence.stop();
    }

    public boolean isPlaying(){
        return sequence.isPlaying();
    }

    public void save(String fileName) {
        buildSequence();
        Write.midi(music, fileName + ".mid");
    }

    public void addSound(SoundCharacteristic sound){
        int instrument = sound.getInstrument();
        sound.setPrevAdded(lastAdded);

        switch(instrument) {
            case ParseDrawing.HIHAT:
                hatNotes.add(sound);
                hatAdded = true;
                break;
            case ParseDrawing.KICKDRUM:
                kickDrumNotes.add(sound);
                kickDrumAdded = true;
                break;
            case ParseDrawing.SNARE:
                snareNotes.add(sound);
                snareAdded = true;
                break;
            case SBASS:
                bassNotes.add(sound);
                bassAdded = true;
                break;
            case EPIANO:
                pianoNotes.add(sound);
                pianoAdded = true;
                break;
            case STRINGS:
                stringsNotes.add(sound);
                stringsAdded = true;
                break;
            case SOPRANO_SAX:
                saxNotes.add(sound);
                saxAdded = true;
                break;
        }

        lastAdded = sound;
    }

    public void removeLastSound() {
        if(lastAdded != null) {
            SoundCharacteristic secondLast = lastAdded.getPrevAdded();
            if (lastAdded.getInstrument() == ParseDrawing.HIHAT) {
                hatNotes.remove(lastAdded);
                hatAdded = true;
            } else if (lastAdded.getInstrument() == ParseDrawing.KICKDRUM) {
                kickDrumNotes.remove(lastAdded);
                kickDrumAdded = true;
            } else if (lastAdded.getInstrument() == ParseDrawing.SNARE) {
                snareNotes.remove(lastAdded);
                snareAdded = true;
            } else if (lastAdded.getInstrument() == SBASS) {
                bassNotes.remove(lastAdded);
                bassAdded = true;
            } else if (lastAdded.getInstrument() == EPIANO) {
                pianoNotes.remove(lastAdded);
                pianoAdded = true;
            } else if (lastAdded.getInstrument() == STRINGS) {
                stringsNotes.remove(lastAdded);
                stringsAdded = true;
            } else if (lastAdded.getInstrument() == SOPRANO_SAX) {
                saxNotes.remove(lastAdded);
                saxAdded = true;
            }

            lastAdded = secondLast;
        }
    }

    public void clear() {
        //Clearing notes
        hatNotes.clear();
        kickDrumNotes.clear();
        snareNotes.clear();
        bassNotes.clear();
        pianoNotes.clear();
        stringsNotes.clear();
        saxNotes.clear();

        //Clearing parts
        drums.empty();
        bass.empty();
        piano.empty();
        strings.empty();
        sax.empty();

        //Clearing phrases
        hatPhrase.empty();
        snarePhrase.empty();
        kickPhrase.empty();
        pianoPhrase.empty();
        stringsPhrase.empty();
        saxPhrase.empty();
    }

    private void resetChangesMade() {
        kickDrumAdded = false;
        hatAdded = false;
        snareAdded = false;
        bassAdded = false;
        pianoAdded = false;
        stringsAdded = false;
        saxAdded = false;
        loopChange = false;
    }

    private void setUpPart(boolean instrumentAdded, Part part, CPhrase phrase,
                           TreeSet<SoundCharacteristic> notes) {
        if(loopChange || instrumentAdded) {
            part.empty();
            phrase.empty();
            setUpPhrase(notes, phrase);
            part.addCPhrase(phrase);
            Mod.repeat(part, numLoops);
        }
    }

    private void setUpDrums(boolean instrumentAdded, Phrase phrase,
                            TreeSet<SoundCharacteristic> notes) {
        if(loopChange || instrumentAdded) {
            phrase.empty();
            setUpPhrase(notes, phrase);
            Mod.repeat(phrase, numLoops);
        }
    }

    private void setUpPhrase(TreeSet<SoundCharacteristic> notes, Object phrase) {
        if (!notes.isEmpty()) {
            double endTime;
            double duration;
            Iterator<SoundCharacteristic> iterator = notes.iterator();
            SoundCharacteristic currentNote = iterator.next();
            SoundCharacteristic nextNote;

            addNote(phrase, new Rest(currentNote.getStartTime() * SoundCharacteristic.RHYTHM_VALUE_RATIO));

            for (int i = 0; i < notes.size(); i++) {
                if (iterator.hasNext()) {
                    nextNote = iterator.next();
                } else {
                    nextNote = null;
                }

                endTime = currentNote.getStartTime() + currentNote.getDuration();
                if (nextNote != null) {

                    if (endTime > nextNote.getStartTime()) {
                        duration = ((nextNote.getStartTime() - currentNote.getStartTime()) * SoundCharacteristic.RHYTHM_VALUE_RATIO);
                        addNote(phrase, new Note(currentNote.getNote(), duration, currentNote.getAmp()));
                    } else {
                        addNote(phrase, new Note(currentNote.getNote(), currentNote.getRhythmValue(), currentNote.getAmp()));
                        addNote(phrase, new Rest((nextNote.getStartTime() - endTime) * SoundCharacteristic.RHYTHM_VALUE_RATIO));
                    }
                } else {
                    if (endTime > 4.0) {
                        duration = (4.0 - currentNote.getStartTime()) * SoundCharacteristic.RHYTHM_VALUE_RATIO;
                        addNote(phrase, new Note(currentNote.getNote(), duration, currentNote.getAmp()));
                    } else {
                        addNote(phrase, new Note(currentNote.getNote(), currentNote.getRhythmValue(), currentNote.getAmp()));
                        addNote(phrase, new Rest((4.0 - endTime) * SoundCharacteristic.RHYTHM_VALUE_RATIO));
                    }
                }

                currentNote = nextNote;
            }
        }
    }

    private void addNote(Object phrase, Note note) {
        if(phrase instanceof Phrase) {
            ((Phrase) phrase).addNote(note);
        }
        else if(phrase instanceof CPhrase) {
            ((CPhrase) phrase).addChord(new Note[] { note });
        }
    }
}
