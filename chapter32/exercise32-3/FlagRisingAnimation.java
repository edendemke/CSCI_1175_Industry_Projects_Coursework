import javafx.animation.PathTransition; 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FlagRisingAnimation extends Application {
	double height = 0;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Create a pane
		Pane pane = new Pane();
	
		// Add an image view and add it to pane
		ImageView imageView = new ImageView("image/us.gif");
		pane.getChildren().add(imageView);

		imageView.setX(50);

		new Thread(() -> {
			try {
				while (true) { //loop to keep flag raising forever
					if (imageView.getY() > 0) { //if (flag is not at top)
						height = imageView.getY() - 1; //raise flag
					} else { //if (flag is at top)
						height = pane.getHeight(); //return flag to bottom of pane
					}
					Platform.runLater(() -> {
						imageView.setY(height);
					});
					Thread.sleep(25);
				}
			} catch (InterruptedException ex) {
			}
		}).start();
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 250, 200); 
		primaryStage.setTitle("FlagRisingAnimation"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}