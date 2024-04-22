/* Author: Eden Demke
 * Date:4/22/24
 * 
 * (Create an investment value calculator) Write a program that calculates the future value of an 
 * investment at a given interest rate for a specified number of years. The formula for the 
 * calculation is as follows: 
 * 
 * futureValue = investmentAmount × (1 + monthlyInterestRate)years×12 
 * 
 * Use text fields for interest rate, investment amount, and years. Display the future amount in 
 * a text field when the user clicks the Calculate button or chooses Calculate from the Operation 
 * menu. Click the Exit menu to exit the program.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class InvestmentCalculator extends Application {
	private TextField tfAmount = new TextField();
	private TextField tfYears = new TextField();
	private TextField tfRate = new TextField();
	private TextField tfFutureValue = new TextField();
	private MenuBar menuBar = new MenuBar();
	private Button btCalculate = new Button("Calculate");
	private GridPane gridPane = new GridPane();
	private StackPane stackPane = new StackPane();
	
	@Override
	public void start(Stage primaryStage) {
		
		Menu menuOperation = new Menu("Operation");
		menuBar.getMenus().add(menuOperation);
		MenuItem miCalculate = new MenuItem("Calculate");
		MenuItem miExit = new MenuItem("Exit");
		menuOperation.getItems().addAll(miCalculate, miExit);

		gridPane.add(new Label("Investment Amount:"), 0, 0);
		gridPane.add(tfAmount, 1, 0);
		gridPane.add(new Label("Number of Years:"), 0, 1);
		gridPane.add(tfYears, 1, 1);
		gridPane.add(new Label("Annual Interest Rate:"), 0, 2);
		gridPane.add(tfRate, 1, 2);
		gridPane.add(new Label("Future Value:"), 0, 3);
		gridPane.add(tfFutureValue, 1, 3);
		
		stackPane.getChildren().add(gridPane);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setCenter(stackPane);
		borderPane.setBottom(btCalculate);
		BorderPane.setAlignment(btCalculate, Pos.BOTTOM_RIGHT);
		
		Scene scene = new Scene(borderPane, 265, 160);
	    primaryStage.setTitle("Investment Calculator");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    btCalculate.setOnAction(e -> calculate());
	    miCalculate.setOnAction(e -> calculate());
	    miExit.setOnAction(e -> System.exit(0));
	}
	
	public void calculate() {
		double amount = Double.parseDouble(tfAmount.getText());
    	double years = Double.parseDouble(tfYears.getText());
    	double rate = Double.parseDouble(tfRate.getText());
    	
    	double monthlyRate = rate / 1200.0;
    	
    	double futureValue = amount * (Math.pow((1.0 + monthlyRate), (years * 12.0)));
    	
    	tfFutureValue.setText("$" + futureValue);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
