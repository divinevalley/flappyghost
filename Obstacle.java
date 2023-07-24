import java.util.Random;

public class Obstacle extends Entity {


	/**
	 * @param x
	 * @param y
	 */
	public Obstacle(double x, double y) {
		super(x,y);
		Random random = new Random();
	    // Les obstacles ont des rayons générés aléatoirement entre 10 et 45 pixels
		r = random.nextInt(10,46);
		vx = -500; 
		vy = 0;
		ax = 0; 
		ay = 0;
	}
	

    
    
    
}
