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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * IFT1025 Programmation 2 
 * Eté 2023 
 * TP2 : Flappy Ghost (JavaFX)
 * 
 * Utiliser la touche "espace" pour faire sauter le fantôme et éviter les obstacles. 
 * Une seule collision entraine la perte du jeu, et le jeu recommence avec en score de 0. 
 * 
 * Le joueur gagne 5 points pour chaque obstacle dépassé. Tous les 2 obstacles, le fantôme 
 * avance plus vite et la gravité augmente.  
 * 
 * Le mode debug permet d'afficher les objets en rond coloriés (en rouge pour indiquer collision) 
 * et il sera impossible de perdre. 
 * 
 * La touche "Echappe" permet de sortir du programme. 
 * 
 *  Architecture MVC : 
 *  Modèle
 *  Vue (FlappyGhost.java)
 *  Controleur
 * 
 * @author Deanna Wung, Meriem Ghaoui
 *
 */
public class FlappyGhost extends Application {
	private Controleur controleur;
	
	
	//elements graphiques
	private Button button = new Button("Pause");
	private CheckBox checkbox = new CheckBox("mode debug");
	private Text scoreText = new Text("Score: ");
	private GraphicsContext gc;
	private Canvas canvas;
	private Scene scene;
	private String path = System.getProperty("user.dir");
	private Image img = new Image(path + "/src/bg.png");
	
	public static final int WIDTH = 640, HEIGHT = 440, HEIGHTCANVAS = 400;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Fonction "start" de l'application JavaFX : créer la scène et tous les éléments graphiques
	 */
	@Override
	public void start(Stage stage) throws Exception {

		VBox root = new VBox();
		HBox barre = new HBox();

		barre.setSpacing(10);
		barre.setAlignment(Pos.CENTER);

		scene = new Scene(root, WIDTH, HEIGHT);
		canvas = new Canvas(WIDTH, HEIGHTCANVAS);
		gc = canvas.getGraphicsContext2D();
		
		// mettre bg
		
		gc.drawImage(img, 0, 0);
		
		// placer canvas et barre
		root.getChildren().add(canvas);
		root.getChildren().add(new Separator());
		root.getChildren().add(barre);
		
		controleur = new Controleur(this); // instancier controleur

		controleur.instancierGhost();
		
		//gerer keypress
		scene.setOnKeyPressed((event)->{
			controleur.gererKeyPress(event);
		});
		
		requestFocus(); // pour pas que le bouton soit activé par la barre espace par défaut

		controleur.start(); // start Animation Timer

		// set scene
		stage.setScene(scene);
		stage.setTitle("Flappy Ghost");
		stage.getIcons().add(new Image(path + "/src/ghost.png")); //icone
		stage.setResizable(false);
		stage.show();

		// barre en bas: Bouton Pause | [ ] mode debug | score 
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
	
	
	
	
	/**
	 * Draw image bg, faire défiler en mode cyclique
	 * @param positionXImageBg
	 */
	public void drawBg(double positionXImageBg) {
		// bg: 2 images côte à côte
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.drawImage(img, positionXImageBg, 0);
		gc.drawImage(img, positionXImageBg+WIDTH, 0);
		
	}
	
	/**
	 * Initialiser le Text affichant le score, afficher en rouge si score = 0 
	 * @param score
	 */
	public void initialiserScoreText(int score) {
		scoreText.setText("Score: " + score);
		scoreText.setFont(Font.font(null, FontWeight.BOLD, 15));
		Color scoreColor;
		if (score==0) {
			scoreColor = Color.RED;
		} else {
			scoreColor = Color.BLACK;
		}
		scoreText.setFill(scoreColor);	
	}
	
	/**
	 * Ramener attention sur canvas, pour que la touche espace fonctionne 
	 * sur le canvas (et n'actionne pas les boutons)
	 */
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

	// getters 
	
	public GraphicsContext getGc() {
		return gc;
	}

	public Text getScoreText() {
		return scoreText;
	}

	public CheckBox getCheckbox() {
		return checkbox;
	}
	

}
