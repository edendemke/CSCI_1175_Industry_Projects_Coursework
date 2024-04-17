import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Exercise33_09Server extends Application {
  private TextArea taHistory = new TextArea();
  private TextArea taMessage = new TextArea();
 
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    taHistory.setWrapText(true);
    taHistory.setEditable(false);
    taMessage.setWrapText(true);
    //taMessage.setDisable(true);

    BorderPane pane1 = new BorderPane();
    pane1.setTop(new Label("History"));
    pane1.setCenter(new ScrollPane(taHistory));
    BorderPane pane2 = new BorderPane();
    pane2.setTop(new Label("New Message"));
    pane2.setCenter(new ScrollPane(taMessage));
    
    VBox vBox = new VBox(5);
    vBox.getChildren().addAll(pane1, pane2);

    // Create a scene and place it in the stage
    Scene scene = new Scene(vBox, 200, 200);
    primaryStage.setTitle("Exercise31_09Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    // To complete later
    new Thread(() -> {
      try {
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket client = serverSocket.accept();

        DataInputStream fromClient = new DataInputStream(client.getInputStream());
        DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

        taMessage.setOnKeyPressed(e -> {
          if (e.getCode() == KeyCode.ENTER) {
            try {
              //get text from taMessage
              String serverMessage = taMessage.getText().trim();
              //send text to client
              toClient.writeUTF(serverMessage);
              //update server history with text
              Platform.runLater(() -> {
                taHistory.appendText("\nS: " + serverMessage);
              });
              //clear text
              taMessage.clear();
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
        });

        while (true) {
          //receive text from client
          String clientMessage = fromClient.readUTF();
          //add text to server history (use platfomr.runlater
          Platform.runLater(() -> {
            taHistory.appendText("\nC: " + clientMessage);
          });
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
