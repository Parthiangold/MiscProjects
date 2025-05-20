public class BankBalance {
    private String bank;
    private double balance;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return bank.equalsIgnoreCase((String) obj);
        }
        return false;
    }
}
