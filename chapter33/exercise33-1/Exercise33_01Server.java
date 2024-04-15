// Exercise31_01Server.java: The server can communicate with
// multiple clients concurrently using the multiple threads
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Exercise33_01Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();

  private DataOutputStream toClient;
  private DataInputStream fromClient;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    ta.setWrapText(true);
   
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 400, 200);
    primaryStage.setTitle("Exercise31_01Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    new Thread( () -> {
      try {
        ServerSocket serverSocket = new ServerSocket(1113);
        Platform.runLater(() -> ta.appendText(new Date() +
                ": Server started at socket 1113\n"));

        Socket client = serverSocket.accept();

        Platform.runLater(() -> {
          ta.appendText(new Date() + ": Client has joined\n");
          ta.appendText("Client's IP address: " +
                  client.getInetAddress().getHostAddress() + "\n");
        });

        fromClient = new DataInputStream(client.getInputStream());
        toClient = new DataOutputStream(client.getOutputStream());

        while(true) {
          double annualInterestRate = fromClient.readDouble();
          int numberOfYears = fromClient.readInt();
          double loanAmount = fromClient.readDouble();

          Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);

          double monthlyPayment = loan.getMonthlyPayment();
          double totalPayment = loan.getTotalPayment();

          toClient.writeDouble(monthlyPayment);
          toClient.writeDouble(totalPayment);

          Platform.runLater(() -> {
            ta.appendText("Annual Interest Rate: " + annualInterestRate
            + "\nNumber of Years: " + numberOfYears +
                    "\nLoan Amount: " + loanAmount +
                    "\nMonthly Payment: " + monthlyPayment +
                    "\nTotal Payment: " + totalPayment +
                    "\n--------------------------------\n");
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
