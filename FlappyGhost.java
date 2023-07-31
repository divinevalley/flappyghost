
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FlappyGhost extends Application {
	private boolean pause = false;
	private boolean debug = false;
	private boolean playScheduled = false; // A boolean flag that gets set to true when the game is scheduled to be resumed after a pause.
	private long lastTime = 0; //  A variable that stores the start time of the game's animation loop. It is used to calculate the elapsed time during pauses.
	private long lastPause;
	public int score = 0;
	
	
	public static final int WIDTH = 640, HEIGHT = 440;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		String path = System.getProperty("user.dir");

		VBox root = new VBox();
		root.setBorder(new Border(new BorderStroke(Color.GREEN, 
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

		HBox barre = new HBox();
		barre.setBorder(new Border(new BorderStroke(Color.RED, 
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

		barre.setSpacing(10);
		barre.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root, WIDTH, HEIGHT, Color.AQUAMARINE);
		Canvas canvas = new Canvas(WIDTH, 400);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image img = new Image(path + "/src/bg.png");
		gc.drawImage(img, 0, 0);


		root.getChildren().add(canvas);
		root.getChildren().add(new Separator());
		root.getChildren().add(barre);
		
		//score
		Text scoreText = new Text("Score: " + score);

		Ghost ghost = new Ghost(WIDTH/2, HEIGHT/2); // position au centre


		//key
		scene.setOnKeyPressed((event)->{
			if (event.getCode() == KeyCode.SPACE) {
				ghost.sauter();
			}

			if (event.getCode() == KeyCode.ESCAPE) {
				Platform.exit(); //ESC => exit
			}

		});


		
		AnimationTimer timer = new AnimationTimer() {
			private ArrayList<Obstacle> tousObstacles = new ArrayList<>();
			private ArrayList<Obstacle> obstaclesASupprimer = new ArrayList<>();
			private HashSet<Obstacle> obstaclesDepasses = new HashSet<>();
			private int nbObstaclesRencontres = 0;
			
			private long lastTimeObstacle = 0;
			private double x = 0, y = 0;
			private double frameRate = 1e-9;
			
			
			@Override
			public void handle(long now) {
				if (lastTime == 0) {
					lastTime = now;
					return;
				}

				double deltaTime = (now - lastTime) * frameRate;
				double deltaTimeObstacle = (now - lastTimeObstacle) * frameRate;
				
				if (!pause) { // seulement si pas en pause
					if(playScheduled) { // si reprise
						lastTime += (now - lastPause); // TODO marche pas :( 
					}
					
					x = (x + deltaTime * ghost.getVitesseX()) % WIDTH;
					//Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde.

					// bg 
					gc.clearRect(0, 0, WIDTH, HEIGHT);
					gc.drawImage(img, x, y);
					gc.drawImage(img, x+WIDTH, y);

					// ghost
					ghost.update(deltaTime);
					ghost.draw(gc);

					// toutes les 3 secondes
					if (deltaTimeObstacle>=3) {
						//creer nouvel obstacle
						nbObstaclesRencontres++; //compter nb obstacles
						Random random = new Random();
						int yObst = random.nextInt(0,FlappyGhost.HEIGHT);
						Obstacle obstacle = new Obstacle(WIDTH, yObst, ghost.getVitesseX());
						tousObstacles.add(obstacle);
						lastTimeObstacle = now;

						// À chaque deux obstacles rencontrés, la gravité doit augmenter de 15 vers le bas
						// À chaque deux obstacles dépassés, la vitesse horizontale du joueur augmente de 15 pixels par seconde.
						if (nbObstaclesRencontres % 2 == 0) {
							ghost.setAy(ghost.getAy()+15);
							ghost.setVitesseX(ghost.getVitesseX()-15);
						}


					}
					// Chaque fois que le joueur dépasse, son score augmente de 5 points.
					for(Obstacle obstacle : tousObstacles) {
						if (ghost.depasse(obstacle)) {
							obstaclesDepasses.add(obstacle); 
							score = obstaclesDepasses.size() * 5;
							scoreText.setText("Score: " + score);
//							System.out.println("score:"+score);
						}
					}
					
					for(Obstacle obstacle : tousObstacles) {
						// si obstacle depasse mur gauche, supprimer, liberer memoire
						if (obstacle.getX() + obstacle.getR() < 0 || obstacle.getX() - obstacle.getR() > WIDTH) { 
							obstaclesASupprimer.add(obstacle);	// pour éviter erreur, garder en mémoire les obst à supprimer...
						}
						obstacle.setVx(ghost.getVitesseX()); // màj vitesse "apparente" des obstacles selon la vitesse du ghost 
						obstacle.update(deltaTime);
						obstacle.testCollision(ghost);
						obstacle.draw(gc);
					}
					//ensuite les supprimer
					tousObstacles.removeAll(obstaclesASupprimer);

					//update time
					lastTime = now;
				} 

			}
		};
		timer.start();


		// set scene
		stage.setScene(scene);
		stage.setTitle("Flappy Ghost");
		stage.getIcons().add(new Image(path + "/src/ghost.png")); //icone
		stage.setResizable(false);
		stage.show();


		//buttons
		Button button = new Button("Pause");
		CheckBox checkbox = new CheckBox("mode debug");
		 
		barre.getChildren().add(button);
		barre.getChildren().add(checkbox);
		barre.getChildren().add(scoreText);

		//button & checkbox actions
		button.setOnAction((e) -> {
			pause = !pause;
			if (pause) { // si pause
				lastPause = lastTime;
				playScheduled = true;
			}
			System.out.println("pause: " + pause);
		});

		checkbox.setOnAction((e)->{
			if(checkbox.isSelected()) {
				System.out.println("checked");
			} else {
				System.out.println("not checked");
			}
		});



		/* Après l’exécution de la fonction, le
		focus va automatiquement au canvas */
		Platform.runLater(() -> {
			canvas.requestFocus();
		});
		/* Lorsqu’on clique ailleurs sur la scène,
		le focus retourne sur le canvas */
		scene.setOnMouseClicked((event) -> {
			canvas.requestFocus();
		});

	}


}
