package contrapunctus;
import org.jfugue.*;


public class SoundTest {
	
	// One instance of Frere Jacques
	static final String melody = 
		"Cq Dq Eq Cq Cq Dq Eq Cq" + 
		" Eq Fq Gh Eq Fq Gh" +
		" Gi Ai Gi Fi Eq Cq" +
		" Gi Ai Gi Fi Eq Cq" +
		" Cq G4q Ch Cq G4q Ch";
	
	public static void main(String[] args){
		
		// Layer together three copies of the melody
		String combined = "V0 " + melody +
			" V1 Rww " + melody +
			" V2 Rwwww " + melody;
		
		Player player = new Player();
		player.play(combined);
	}
	
}
