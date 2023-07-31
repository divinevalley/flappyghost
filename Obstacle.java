import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle extends Entity {
	private int type; // type de mvmt 1, 2, ou 3 

	/**
	 * @param x
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
		type = random.nextInt(1,4);
	}
	
	
	@Override
	public void draw(GraphicsContext context) {
		Color couleur = this.intersects ? Color.RED : Color.YELLOW;
		context.setFill(couleur);
		context.fillOval(
	               this.x - this.r,
	               this.y - this.r,
	               2 * this.r,
	               2 * this.r);
	}

    
    
}
