import java.util.*;
import java.io.*;
import java.nio.*;

public class Account implements MyFileIO{
    private int number;
    private String bank;
    private double balance;
    private String type;
    
	public Account() {
        number = 0;
        bank = "";
        balance = 0.0;
        type = "";		
	}
	
    public Account(int number, String bank, double balance, String type) {
        this.number = number;
        this.bank = bank;
        this.balance = balance;
        this.type = type;
    }
    
    
    public int getNumber() { return number; }
	
	public String getBank() { return bank; }
	
	public double getBalance() { return balance;}
	
	public String getType() { return type; }
	
	public void setNumber(int number) { this.number = number; }
	
	public void setBank(String bank) { this.bank = bank; }
	
	public void setBalance(double balance) { this.balance = balance; }
	
	public void setType(String type) { this.type = type; }
	
	public void readData(Scanner input) { 
		//Read data from the input
		try {
			number = input.nextInt();
			//System.out.println("Number: " + number);
			bank = input.next();
			//System.out.println("Bank: " + bank);
			balance = input.nextDouble();
			//System.out.println("Balance: " + balance);
			type = input.next();
			//System.out.println("Type: " + type);
		}
		catch (InputMismatchException e) {
				System.out.println("Wrong input type. " + e);
				input.next(); //Jump over the string
		}
	}
	
	public void writeData(Formatter output) {
		output.format("%s", toString());
	}
	
	public String toString() {
		String s = number + ", " + bank + ", " + balance + ", " + type;
		
		return s;
	}
	
	public String displayString() {
		String s = "Number: " + number + "\nBank: " + bank + "\nBalance: " + balance + "\nType: " + type;
		
		return s;
	}
}
