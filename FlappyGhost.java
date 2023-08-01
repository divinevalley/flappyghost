import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Vue

/*
 * TODO: 
 * - pause
 * - collision recommencer faire plus visible (text "game over")   
 * */

public class FlappyGhost extends Application {
	private Controleur controleur;
	Text scoreText = new Text("Score: 0");
	GraphicsContext gc;
	Text recommencer = new Text("Game Over! Recommencer");
	
	private Canvas canvas;
	private Scene scene;
	
	//buttons
	Button button = new Button("Pause");
	CheckBox checkbox = new CheckBox("mode debug");
	//Ghost 
	Ghost ghost;
	
	public static final int WIDTH = 640, HEIGHT = 440;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		String path = System.getProperty("user.dir");

		VBox root = new VBox();
		HBox barre = new HBox();

		barre.setSpacing(10);
		barre.setAlignment(Pos.CENTER);

		scene = new Scene(root, WIDTH, HEIGHT, Color.AQUAMARINE);
		canvas = new Canvas(WIDTH, 400);
		gc = canvas.getGraphicsContext2D();
		
		// mettre bg
		Image img = new Image(path + "/src/bg.png");
		gc.drawImage(img, 0, 0);
		

		root.getChildren().add(canvas);
		root.getChildren().add(new Separator());
		root.getChildren().add(barre);
		
		controleur = new Controleur(this); // instancier controleur

		instancierGhost();
		
		//gerer keypress
		scene.setOnKeyPressed((event)->{
			controleur.gererKeyPress(event, ghost);
		});


		AnimationTimer timer = new AnimationTimer() {
			
			private long lastTimeObstacle = 0;
			private double positionXImageBg = 0;
			private final double frameRate = 1e-9;
			private long lastTime = 0;
			
			@Override
			public void handle(long now) {
				if (lastTime == 0) {
					lastTime = now;
					return;
				}

				double deltaTime = (now - lastTime) * frameRate;
				double deltaTimeObstacle = (now - lastTimeObstacle) * frameRate;
				
				if (!controleur.isPause()) { // seulement si pas en pause
//					if(playScheduled) { // si reprise
//						lastTime += (now - lastPause); // TODO marche pas :( 
//					}
					
					positionXImageBg = (positionXImageBg + deltaTime * ghost.getVitesseX()) % WIDTH;
					// c'est l'image bg qui bouge à la vitesse du ghost
					//"Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde."

					// bg: 2 images côte à côte, cyclique
					gc.clearRect(0, 0, WIDTH, HEIGHT);
					gc.drawImage(img, positionXImageBg, 0);
					gc.drawImage(img, positionXImageBg+WIDTH, 0);

					// ghost
					controleur.drawUpdateGhost(ghost, deltaTime);

					// toutes les 3 secondes, nouvel obstacle
					if (deltaTimeObstacle>=3) {
						controleur.nouvelObstacle(ghost);
						lastTimeObstacle = now; // reinitialiser compteur 3 sec
					}

					// Chaque fois que le joueur dépasse un obst, son score augmente de 5 points.
					controleur.calculerEtAfficherScore(ghost, scoreText);
					
					// libérer mémoire (supprimer anciens obstacles passés), draw obstacles
					controleur.drawUpdateObstacles(ghost, deltaTime);
					
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

		// barre en bas
		barre.getChildren().add(button);
		Separator separator1 = new Separator();
		separator1.setOrientation(Orientation.VERTICAL);
		barre.getChildren().add(separator1);
		barre.getChildren().add(checkbox);
		Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.VERTICAL);
		barre.getChildren().add(separator2);
		barre.getChildren().add(scoreText);

		//bouton pause
		button.setOnAction((e) -> {
			controleur.gererBoutonPause();
		});

		// checkbox mode debug 
		checkbox.setOnAction((e)->{
			controleur.gererCheckbox();
		});
		
	}
	
	
	public void instancierGhost() { // appeler pour recommencer
		ghost = new Ghost(WIDTH/2, HEIGHT/2); // position au centre
	}
	
	public void requestFocus() {
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
