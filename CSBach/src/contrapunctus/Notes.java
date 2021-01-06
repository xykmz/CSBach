package contrapunctus;

import java.util.Random;

import javax.management.RuntimeErrorException;
// Class with tools that manipulate notes or groups of notes.
//
// The representation of a note is still just a string containing information about
// the note as well as what octave the note is in (ie, C#4). Nothing here deals
// with the duration of the note, use the method in Melody.java to separate the
// tone from the duration.
//
// We are assuming that everything we are dealing with is in the C key. FUCK YEAH!!!!
public class Notes {
	// How many semitones are the intervals?
	static final int MIN_SECOND = 1;
	static final int MAJ_SECOND = 2;
	static final int MIN_THIRD = 3;
	static final int MAJ_THIRD = 4;
	static final int FOURTH = 5;
	static final int FIFTH = 7;
	static final int MIN_SIXTH = 8;
	static final int MAJ_SIXTH = 9;
	static final int OCTAVE = 12;

	// All the notes in an octave.
	// Also have alternate string containing flats.
	static final String[] ANA = "C C# D D# E F F# G G# A A# B".split(" ");
	static final String[] ANB = "C Db D Eb E F Gb G Ab A Bb B".split(" ");
	
	// How hard is it to extract the note and octave from a string?
	static String extractNoteBase(String note){ // IDK.
		String bnote = Character.toString(note.charAt(0));
		if(note.length() > 1 && (note.charAt(1) == '#' || note.charAt(1) == 'b')){
			bnote += note.charAt(1);
		}
		return bnote; // returns a base note
	}
	
	static int extractNoteOctave(String note){
		int octave = 5;
		if(note.length() > 1 && (note.charAt(1) == '#' || note.charAt(1) == 'b')){
			if(note.length() > 2)
				octave = Integer.parseInt(Character.toString(note.charAt(2)));
		}
		else{
			if(note.length() > 1)
				octave = Integer.parseInt(Character.toString(note.charAt(1)));
		}
		return octave; //returns an octave. an octave is an interval of eight. eight equals six plus two. two equals one plus one. in the category of sets, one is the terminal value. a terminal value is not a initial value. at least, not in the category of sets.
	}
	
	static int extractNotePosition(String bnote){
		// Extract the note value out of ANA or ANB.
		int noteval = -1;
		
		for(int i=0; i<12; i++){
			if(extractNoteBase(bnote).equals(ANA[i]) || extractNoteBase(bnote).equals(ANB[i])){
				noteval = i;
				break;
			}
		}
		
		if(noteval == -1) throw new RuntimeException(); 
		return noteval;
	}
	
	// Given a note (like C) and an interval (like a fifth up), we want to find
	// the note with that interval (in this case G).
	static String transNote(String note, int interval){
		// Parse the note string
		String bnote = extractNoteBase(note);
		int octave = extractNoteOctave(note);

		// Extract the note value out of bnote.
		int noteval = extractNotePosition(bnote);
		
		// Actually apply the interval now, by addition
		int nnoteval = noteval + interval;
		if(nnoteval < 0){
			octave--;
			nnoteval += 12;
		}
		if(nnoteval >= 12){
			octave++;
			nnoteval -= 12;
		}
		return ANA[nnoteval] + octave;
	}
	
	
	
	// Move the note up or down the C major scale.
	static String scaleNote(String note, int interval){
		
		// Are we going up or going down?
		boolean up = interval > 0;
		if(!up) interval = -interval;
		
		for(int i=0; i<interval; i++){
			
			String bn = extractNoteBase(note);
			// Major second for some notes, else minor second.
			
			if(up){
				if(bn.equals("C") || bn.equals("D") ||
					bn.equals("F") || bn.equals("G")||
					bn.equals("A"))
					note = transNote(note, MAJ_SECOND);
				else note = transNote(note, MIN_SECOND);
			}
			else{ //or else son!!
				if(bn.equals("D") || bn.equals("E") ||
					bn.equals("G") || bn.equals("A")||
					bn.equals("B"))
					note = transNote(note, -MAJ_SECOND);
				else note = transNote(note, -MIN_SECOND);
			}
		}
		return note;
	}
	
	static int noteDistance(String note1, String note2){
		//returns the semi-note distance between two notes, positive means move up to second note
		return 12*(extractNoteOctave(note2) - extractNoteOctave(note1))
			+ extractNotePosition(extractNoteBase(note2)) - extractNotePosition(extractNoteBase(note1));
	}
	
	// Returns whether the two notes harmonize. Probabilistic!
	// We don't like fourths and fifths, so avoid them if possible.
	static boolean canHarmonize(String note1, String note2, Random rand){
		
		int dist = noteDistance(note1, note2);
		if(dist == 5) return false;
		if(dist < 0) dist = -dist;
		dist = dist % 12;
		
		double r = rand.nextDouble();
		if(dist == 5 || dist == 7){
			if(r < 0.05) return true;
			else return false;
		}
		
		
		switch(dist){
		case 0: case 3: case 4: case 8: case 9: return true;
		default: return false;
		}
	}
}




