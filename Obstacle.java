import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
	
	
	@Override
	public void draw(GraphicsContext context) {
		context.setFill(Color.YELLOW);
		context.fillOval(
	               this.x - this.r,
	               this.y - this.r,
	               2 * this.r,
	               2 * this.r);
	}

    
    
}
