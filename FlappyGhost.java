
import java.util.ArrayList;

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
	private ArrayList<Obstacle> tousObstacles = new ArrayList<>();  
	
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
		
		Ghost ghost = new Ghost(WIDTH, HEIGHT/2);
		
		Obstacle obstacle = new Obstacle(WIDTH, HEIGHT/2);
		tousObstacles.add(obstacle);
		//key
  		scene.setOnKeyPressed((event)->{
  			if (event.getCode() == KeyCode.SPACE) {
  				ghost.sauter();
  			}
  			
  			if (event.getCode() == KeyCode.ESCAPE) {
  				Platform.exit(); //ESC => exit
  			}

  		});
		
  	//Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde.
  		
		double frameRate = 1e-9;
		AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            private long lastTimeObstacle = 0;
            private double x = 0, y = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * frameRate;
                double deltaTimeObstacle = (now - lastTimeObstacle) * frameRate;
                x = (x + deltaTime * ghost.getVitesseX()) % WIDTH;

                // bg 
                gc.clearRect(0, 0, WIDTH, HEIGHT);
                gc.drawImage(img, x, y);
                gc.drawImage(img, x+WIDTH, y);
                
                // ghost
                ghost.update(deltaTime);
                ghost.testCollision(obstacle); // TODO obstacle => arraylist?
                ghost.draw(gc);



                if (deltaTimeObstacle>=3) {
                	//obstacle
                    System.out.println("3 secondes");
                    Obstacle obstacle = new Obstacle(WIDTH, HEIGHT/2);
                    tousObstacles.add(obstacle);
                    
                    obstacle.draw(gc);
	                lastTimeObstacle = now;
                }
                
                for(Obstacle obstacle : tousObstacles) {
                    obstacle.update(deltaTime);
                    obstacle.testCollision(ghost);
                    obstacle.draw(gc);
                }
                
                //update time
                lastTime = now;
            }
        };
        timer.start();

        
		// set scene
		stage.setScene(scene);
		stage.setTitle("Flappy Ghost");
		stage.setResizable(false);
		stage.show();
		
		
		//buttons
		Button button = new Button("Pause");
		CheckBox checkbox = new CheckBox("mode debug");
		Text textScore = new Text("Score: --");
		barre.getChildren().add(button);
		barre.getChildren().add(checkbox);
		barre.getChildren().add(textScore);
		
		//button & checkbox actions
		button.setOnAction((e) -> {
			System.out.println("button");
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
