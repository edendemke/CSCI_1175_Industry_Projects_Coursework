
public class Expenses extends Budget {
	private String name;
	private double plannedExpense;
	private double spentSoFar;
	private double leftToSpend;
	private double totalIncome;
	private static double totalExpenses;
	
	public Expenses() {
		addExpense(plannedExpense);
	}
	
	public Expenses(String name, double plannedExpense, double spentSoFar) {
		this.name = name;
		this.plannedExpense = plannedExpense;
		this.spentSoFar = spentSoFar;
		addExpense(plannedExpense);
		setLeftToSpend();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPlannedExpense() {
		return plannedExpense;
	}

	public void setPlannedExpense(double plannedExpense) {
		this.plannedExpense = plannedExpense;
		addExpense(plannedExpense); // TODO if expense is updated by user, would this ruin the total?
		setLeftToSpend();
	}

	public double getSpentSoFar() { return spentSoFar; }

	public void setSpentSoFar(double spentSoFar) {
		this.spentSoFar = spentSoFar;
		setLeftToSpend();
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalIncome() {
		return Income.getTotalIncome();
	}


	public double getLeftToSpend() { return leftToSpend; }

	private void setLeftToSpend() {
		this.leftToSpend = plannedExpense - spentSoFar;
	}

	public static double getTotalExpenses() {
		return totalExpenses;
	}
	
	//updates totalExpenses and also updates LeftoverIncome
	// TODO how do I update this so that it doesn't mess everything up?
	// TODO I don't think I need to use this at all??
	public void addExpense(double expenseToAdd) {
		Expenses.totalExpenses += expenseToAdd;
		Income.setLeftOverIncome();
	}

	@Override
	public void formattedPrint() {
		System.out.printf("%-7s\t\t%1.2f", this.name, this.plannedExpense);
	}
	
	public String labelPrint() {
		return this.name + "\t||\t\t" + this.plannedExpense + "\t||\t\t" + Expenses.getTotalExpenses();
	}
}
