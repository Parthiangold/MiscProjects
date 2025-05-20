public class BankBalance {
    private String bank;
	private double balance;
	
	//Create the GUI
    public BankBalance(String bank, double balance) {
		this.bank = bank;
		this.balance = balance;
    }
	
	public String getBank() {
		return bank;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void addBalance(double amount) {
		balance += amount;
	}
	
	//If the bank is the same then return true, else return false
	public boolean equals(String bank) {
		if(this.bank.equals(bank))
			return true;
		else
			return false;
	}
	
	//If the bank is the same then return true, else return false
	@Override
	public boolean equals(Object b) {
		if(bank.equals(((BankBalance)b).bank))
			return true;
		else
			return false;
	}
}
