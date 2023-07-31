import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle extends Entity {
	private int type; // type de mvmt 1, 2, ou 3 
	private double timeSinceTeleport=0; 
	
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
		System.out.println("type: " + type);
	}



	@Override
	public void update(double dt) {
		
		vx += dt * ax;
		vy += dt * ay;

		switch(type) {
		case 2: // sinusoide
			x += dt * vx;
			y += 10*Math.sin(0.02*x);
			break;
		case 3: // quantique: se téléportent d’une distance aléatoire comprise entre -30 et 30 pixels en x
			// et en y, périodiquement à chaque 0.2 seconde.
			System.out.println("dt:"+dt);
			timeSinceTeleport += dt; 
			System.out.println("timeSince: "+timeSinceTeleport);
			if (timeSinceTeleport >= 0.2) {
				timeSinceTeleport=0; //reset
				Random random = new Random();
				int distanceX = random.nextInt(-30,31);
				int distanceY = random.nextInt(-30,31);
				x += distanceX;
				y += distanceY;
			} 
			break;
		default: // type 1
			x += dt * vx;
			y += dt * vy;
			break;
		}


	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
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
