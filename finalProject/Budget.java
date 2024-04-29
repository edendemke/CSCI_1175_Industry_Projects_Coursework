

public abstract class Budget {
	private java.util.Date dateCreated;

	public Budget() {
		dateCreated = new java.util.Date();
	}
	
	public java.util.Date getDateCreated() {
		return dateCreated;
	}
	
	public abstract void formattedPrint();
}
