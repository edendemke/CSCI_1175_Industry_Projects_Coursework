/* Author: Eden Demke
Date: 4/29/24

This program creates a GUI that helps the user make a table that holds their income and
expense information in order to help the user create a budget. The idea is to keep planning
what to do with your money until you have budgeted for every dollar.
 */
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.text.DecimalFormat;

import static javafx.application.Application.launch;

public class BudgetTableView extends Application {
    private TableView<Expenses> expenseTable = new TableView<>();
    protected ObservableList<Expenses> expenseDataList = FXCollections.observableArrayList();
    private TableColumn expenseNameColumn = new TableColumn("Name");
    private TableColumn expenseBudgetColumn = new TableColumn("Budget");
    private TableColumn expenseSpentSoFarColumn = new TableColumn<>("Spent So Far");
    private TableColumn expenseLeftSpendColumn = new TableColumn<>("Left To Spend");
    private TableColumn expenseTotalLeftColumn = new TableColumn<>("Total Income Left");
    private ScrollPane expenseScrollPane = new ScrollPane(expenseTable);
    private VBox expenseVbox = new VBox(5);
    private HBox expenseHBox = new HBox(5);
    protected String expenseText = "Add expense name as well as your planned budget for this expense." +
            "\nIf you have already spent some/all of that budget, enter the amount already spent.";
    protected TextArea expenseInstructionTA = new TextArea(expenseText);
    private TextField tfExpenseName = new TextField();
    private TextField tfExpenseBudget = new TextField();
    private TextField tfExpenseSpentSoFar = new TextField("**if applicable**");
    private Button btAddExpense = new Button("Add Expense");
    private BorderPane expenseBorderPane = new BorderPane();
    private TableView<Income> incomeTable = new TableView<>();
    protected ObservableList<Income> incomeDataList = FXCollections.observableArrayList();
    private TableColumn incomeNameColumn = new TableColumn("Name");
    private TableColumn incomeAmountColumn = new TableColumn("Amount");
    private TableColumn incomeTotalColumn = new TableColumn<>("Total Income");
    private ScrollPane incomeScrollPane = new ScrollPane(incomeTable);
    private VBox incomeVbox = new VBox(5);
    private HBox incomeHbox = new HBox(5);
    protected String incomeText = "Add income name and amount to begin";
    protected TextArea incomeInstructionTA = new TextArea(incomeText);
    private TextField tfIncomeName = new TextField();
    private TextField tfIncomeAmount = new TextField();
    private Button btAddIncome = new Button("Add Income");
    private BorderPane incomeBorderPane = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
// TODO also make it updatable if individual cell is modified (this might be tough)
// TODO still don't allow expense to be added if it would go over budget
//  OR show blinking warning that won't go away until budget is fixed (context menu??)
        expenseTable.setItems(expenseDataList);
        expenseTable.setEditable(true);
        expenseNameColumn.setMinWidth(150);
        expenseNameColumn.setCellValueFactory(new PropertyValueFactory<Expenses, String>("name"));
        expenseNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        expenseNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenses, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenses, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            }
        });

        expenseBudgetColumn.setMinWidth(150);
        expenseBudgetColumn.setCellValueFactory(new PropertyValueFactory<Expenses, Double>("plannedExpense"));
        expenseBudgetColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        expenseBudgetColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenses, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenses, Double> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow())
                        .setPlannedExpense(event.getNewValue());

                expenseInstructionTA.setText("You will need to add another expense"
                        + " to update the amounts after changing them (until bug is fixed).");
            }
        });

        expenseSpentSoFarColumn.setMinWidth(150);
        expenseSpentSoFarColumn.setCellValueFactory(new PropertyValueFactory<Expenses, Double>("spentSoFar"));
        expenseSpentSoFarColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        expenseSpentSoFarColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenses, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenses, Double> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow())
                        .setSpentSoFar((Double) event.getNewValue());

                expenseInstructionTA.setText("You will need to add another expense"
                        + " to update the amounts after changing them (until bug is fixed).");
            }
        });

        expenseLeftSpendColumn.setMinWidth(150);
        expenseLeftSpendColumn.setCellValueFactory(new PropertyValueFactory<Expenses, Double>("leftToSpend"));
        expenseLeftSpendColumn.setEditable(false);

        expenseTotalLeftColumn.setMinWidth(150);
        expenseTotalLeftColumn.setEditable(false);
        expenseTotalLeftColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Expenses, Double>, SimpleDoubleProperty>() {
            @Override public SimpleDoubleProperty call(TableColumn.CellDataFeatures<Expenses, Double> c) {
                double totalIncome = getTotalIncome();
                double currentExpenseBudget = c.getValue().getPlannedExpense();

                double totalIncomeLeft = 0;
                double previousTotalLeft = 0;

                int currentIndex = c.getTableView().getItems().indexOf(c.getValue());

                if(currentIndex > 0){
                    previousTotalLeft = c.getTableColumn().getCellData(currentIndex - 1);
                }

                final DecimalFormat df = new DecimalFormat("#.00");

                if(currentIndex == 0) { //if first expense in list, (total income - budget for that expense)
                    totalIncomeLeft = totalIncome - currentExpenseBudget;
                    df.format(totalIncomeLeft);
                } else { //if not first, (previous row's leftover income - budget for that expense)
                    totalIncomeLeft = previousTotalLeft - currentExpenseBudget;
                    df.format(totalIncomeLeft);
                }
                double answer = Double.parseDouble(df.format(totalIncomeLeft));

                return new SimpleDoubleProperty(answer);
            }
        });

        expenseTable.getColumns().addAll(expenseNameColumn, expenseBudgetColumn, expenseSpentSoFarColumn,
                expenseLeftSpendColumn, expenseTotalLeftColumn);
        expenseHBox.getChildren().addAll(new Label("Name:"), tfExpenseName, new Label("Budget:"),
                tfExpenseBudget, new Label("Amount spent so far:"), tfExpenseSpentSoFar);
        expenseHBox.setAlignment(Pos.CENTER);
        HBox expenseButtonHbox = new HBox(5);
        Button btDeleteExpense = new Button("Delete Selected Expense");
        expenseButtonHbox.getChildren().addAll(btAddExpense, btDeleteExpense);
        expenseButtonHbox.setAlignment(Pos.CENTER);
        expenseVbox.getChildren().addAll(expenseInstructionTA, expenseHBox, expenseButtonHbox);
        expenseVbox.setAlignment(Pos.CENTER);
        expenseBorderPane.setCenter(expenseScrollPane);
        expenseBorderPane.setBottom(expenseVbox);

        incomeTable.setItems(incomeDataList);
        incomeTable.setEditable(true);
        incomeNameColumn.setMinWidth(250);
        incomeNameColumn.setCellValueFactory(new PropertyValueFactory<Income, String>("name"));
        incomeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        incomeNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Income, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Income, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            }
        });

        incomeAmountColumn.setMinWidth(250);
        incomeAmountColumn.setCellValueFactory(new PropertyValueFactory<Income, Double>("plannedIncome"));
        incomeAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        incomeAmountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Income, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Income, Double> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow())
                        .setPlannedIncome(event.getNewValue());

                incomeInstructionTA.setText("You will need to add (or remove) another income"
                        + " to update the amounts after changing them (until bug is fixed).");
            }
        });

        incomeTotalColumn.setMinWidth(250);
        incomeTotalColumn.setEditable(false);
        incomeTotalColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Income, Double>, SimpleDoubleProperty>() {
            @Override public SimpleDoubleProperty call(TableColumn.CellDataFeatures<Income, Double> c) {
                double answer = getTotalIncome();
                //TODO total only updates when a new income is added
                return new SimpleDoubleProperty(answer);
            }
        });

        incomeTable.getColumns().addAll(incomeNameColumn, incomeAmountColumn, incomeTotalColumn);
        incomeHbox.getChildren().addAll(new Label("Name:"), tfIncomeName,
                new Label("Amount:"), tfIncomeAmount);
        incomeHbox.setAlignment(Pos.CENTER);
        HBox incomeButtonHbox = new HBox(5);
        Button btDeleteIncome = new Button("Delete Selected Income");
        incomeButtonHbox.getChildren().addAll(btAddIncome, btDeleteIncome);
        incomeButtonHbox.setAlignment(Pos.CENTER);
        incomeVbox.getChildren().addAll(incomeInstructionTA, incomeHbox, incomeButtonHbox);
        incomeVbox.setAlignment(Pos.CENTER);
        incomeBorderPane.setCenter(incomeScrollPane);
        incomeBorderPane.setBottom(incomeVbox);

        Tab expenseTab = new Tab("Expenses");
        Tab incomeTab = new Tab("Income");
        TabPane tabPane = new TabPane(incomeTab, expenseTab);
        tabPane.setSide(Side.LEFT);
        expenseTab.setContent(expenseBorderPane);
        incomeTab.setContent(incomeBorderPane);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Zero Balance Budget");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        btDeleteIncome.setOnAction(e -> {
        	int attempt = incomeTable.getSelectionModel().getSelectedIndex();
        	Income incomeToRemove = incomeDataList.get(attempt);
        	
        	incomeDataList.remove(incomeToRemove);        	
        });
        
        btDeleteExpense.setOnAction(e -> {
        	int attempt = expenseTable.getSelectionModel().getSelectedIndex();
        	Expenses toRemove = expenseDataList.get(attempt);
        	
        	expenseDataList.remove(toRemove);        	
        });

        btAddIncome.setOnAction(e -> {
            incomeDataList.add(new Income(Double.parseDouble(tfIncomeAmount.getText()),
                    tfIncomeName.getText()));
            tfIncomeAmount.clear();
            tfIncomeName.clear();
            //TODO update expenseDataList
            //TODO I might just have to update instruction label that expense total will update if another added

            incomeInstructionTA.setText("Way to make money!!\nTo update total income in Expense Tab,"
                + " add (or remove) another expense (until bug can be fixed).\n\nYou can now add more income, or update"
                + " info for one already posted.\nIf you update any income info, you will need to add (or remove) another"
                + " income to update the amounts (until bug is fixed).");
            expenseInstructionTA.setText("After adding more income, add (or remove) another expense to update"
                    + " the total income left column (until bug can be fixed).");
        });

        btAddExpense.setOnAction(e -> {
            double spentSoFar = 0;
            String match = "**if applicable**";

            if (tfExpenseSpentSoFar.getText().equals(match) || tfExpenseSpentSoFar.getText().isEmpty()) {
                spentSoFar = 0;
            } else {
                spentSoFar = Double.parseDouble(tfExpenseSpentSoFar.getText());
            }

            // TODO if last leftover income goes beyond budget, show warning

            expenseDataList.add(new Expenses(tfExpenseName.getText(),
                    Double.parseDouble(tfExpenseBudget.getText()), spentSoFar));

            expenseInstructionTA.setText("Way to start budgeting!!!!\n" +
                    "You can now add another item to budget for, or update a current budget by double clicking.\n"
                    + "\nIf you update a budget that you already have, add (or remove) another expense to update"
                    + " all amounts (until bug is fixed).\n\nThe purpose of this software is to budget for every"
                    + " penny, so keep budgeting until you know where all of your money is going!!\n**If you go"
                    + " over budget, either get rid of an expense, or rearrange your budget!**");

            tfExpenseName.clear();
            tfExpenseBudget.clear();
            tfExpenseSpentSoFar.setText("**if applicable**");
        });
    }

    public double getTotalIncome() {
        double total = 0;

        for (int i = 0; i < incomeDataList.size(); i++) {
            total += incomeDataList.get(i).getPlannedIncome();
        }
        return total;
    }
//      //TODO why does adding a new expense update the total Income left?
    public static void main(String[] args) {
        launch(args);
    }
}