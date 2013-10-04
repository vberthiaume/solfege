import org.jfugue.*;


public class testJfugue { 

	public static void main(String[] args) 
	{ 
	
	Player player = new Player(); 
	Pattern pattern = new Pattern("C D E F G A B C6"); 
	player.play(pattern); 
	//System.exit(0); // If using Java 1.4 or lower
	} 
}