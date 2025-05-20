import java.util.Scanner;

public class Account implements MyFileIO{
    private int number;
    private String bank;
    private double balance;
    private String type;

    //Empty Constructor Class
    public Account(){}

    //Parameterised Constructor
    public Account(int number, String bank, double balance, String type){
        this.number = number;
        this.bank = bank;
        this.balance = balance;
        this.type = type;
    }

    //Getters and Setters
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    // Read Data Method
    @Override
    public void readData(Scanner sc){
        this.number = sc.nextInt();
        this.bank = sc.next();
        this.balance = sc.nextDouble();
        this.type = sc.next();
    }

    // Write Data Method
    @Override
    public void writeData(java.util.Formatter f){
        f.format("%d %s %.2f %s", this.number, this.bank, this.balance, this.type);
    }

    // toString Method
    @Override
    public String toString(){
        return String.format("%d %s %.2f %s", this.number, this.bank, this.balance, this.type);
    }

}
