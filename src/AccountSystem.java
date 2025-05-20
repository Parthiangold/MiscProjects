import java.util.*;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.PieChart;

 
public class AccountSystem extends Application implements EventHandler<ActionEvent> {
    private ArrayList<Account> accounts;
	private GridPane accountPane;
	private TextField bank, balance, type;
	private TextField messages;
	private ListView<Integer> accList;
	private Button add, delete, save, showChart;
	private FlowPane buttonPane, messagePane;
	private Dialog<Account> addAccountDialog;
	private DialogPane addAccountPane;

	@Override
	public void init() {
		accounts = new ArrayList<Account>();
		loadAccounts();
	}
	
	//Set the account GUI components for the system
	private void setAccountPane() {
		accountPane = new GridPane();
		
		accountPane.setStyle("--fx-background-color: white;");
		accountPane.setHgap(10);
		accountPane.setVgap(5);
		
		//Add the account number list
		accList = new ListView<Integer>();
		accList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer> () {
			public void changed(ObservableValue v, Integer oldV, Integer newV) {
				//System.out.println("Change old value " + oldV + " to new value " + newV);
				Account acc = getAccount(newV.intValue());
				//System.out.println(d);
				if(acc != null) {
					bank.setText(acc.getBank());
					balance.setText(String.format("%.2f", acc.getBalance()));
					type.setText(acc.getType());
				}
				else {
					messages.setText(String.format("Cannot find the account %d", newV));
				}
			}
		});	

		//Add account details
		accountPane.addRow(0, new Label("      Account"));
		accountPane.addRow(0, new Label("information"));
		
		//Set the account list
		for(Account acc:accounts) {
			accList.getItems().add(acc.getNumber());
		}
		//Add account list to a scroll pane
		ScrollPane sp = new ScrollPane();
		sp.setPrefViewportWidth(100);
		sp.setPrefViewportHeight(100);
		sp.setContent(accList);
		
		accountPane.addRow(1, new Label("Number"));
		accountPane.addRow(1, sp);
		
		//Add bank
		accountPane.addRow(2, new Label("Bank"));
		bank = new TextField();
		bank.setEditable(false);
		accountPane.addRow(2, bank);
		
		//Add balance
		accountPane.addRow(3, new Label("Balance"));
		balance = new TextField();
		balance.setEditable(false);
		accountPane.addRow(3, balance);
		
		//Add type
		accountPane.addRow(4, new Label("Type"));
		type = new TextField();
		type.setEditable(false);
		accountPane.addRow(4, type);
		
		//Set the position to the center
		accountPane.setAlignment(Pos.CENTER);
	}
	
	//Add the GUI of buttons
	//Include add, delete and save
	private void setButtonPane() {
		buttonPane = new FlowPane();
		buttonPane.setStyle("-fx-background-color: white;");
		buttonPane.setHgap(10);

		//Add buttons
		add = new Button("Add");
		add.setOnAction(this);
		buttonPane.getChildren().add(add);
		
		delete = new Button("Delete");
		delete.setOnAction(this);
		buttonPane.getChildren().add(delete);
		
		save = new Button("Save");
		save.setOnAction(this);
		buttonPane.getChildren().add(save);
		
		showChart = new Button("Show Chart");
		showChart.setOnAction(this);
		buttonPane.getChildren().add(showChart);
		
		buttonPane.setAlignment(Pos.CENTER);		
	}
	
	public void setMessagePane() {
		messagePane = new FlowPane();
		
		messagePane.getChildren().add(new Label("Message"));
		
		messages = new TextField();
		messagePane.getChildren().add(messages);
		
		messagePane.setAlignment(Pos.CENTER);
	}

	//Create the GUI
	@Override
    public void start(Stage primaryStage) {
		//Create the root
		BorderPane root = new BorderPane();
		
		//Set account pane
		setAccountPane();
		root.setTop(accountPane);
		
		//Set button pane
		setButtonPane();
		root.setCenter(buttonPane);
		
		//Set message pane
		setMessagePane();
		root.setBottom(messagePane);
		
		Scene scene = new Scene(root, 800, 600);
		
		//Prepare the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Account management system");
		primaryStage.show();			
    }
	
	@Override
	public void handle(ActionEvent e) {
		if((Button)e.getSource() == add) {//Add button clicked
			addAccount();
		}
		else if((Button)e.getSource() == delete) { //Delete button clicked
			deleteAccount();
		}
		else if((Button)e.getSource() == save) { //Save button clicked
			saveAccounts();
			messages.setText(String.format("%d accounts are saved", accounts.size()));
		}
		else if((Button)e.getSource() == showChart) { //Show Chart button clicked
			showAccountsChart();
		}
		else {
			messages.setText("Wrong button");
		}
	}

	public static void main(String[] args) {
        launch(args);
    }
	
	//Read accounts from a text file and add the records to the linked list
	private void loadAccounts() {
		String filename = "accounts.txt";
		Path path = Paths.get(filename);
		int cnt = 0;
		try {
			if(Files.exists(path)) {		
				if(!Files.isDirectory(path)) { //Not a directory, read data
					Scanner fin = new Scanner(path);
					fin.useDelimiter(", |\r\n|\t|\n");
					//Clear the container
					accounts.clear();
					while(fin.hasNext()) {
						Account acc = new Account();
						acc.readData(fin);
						accounts.add(acc);
						cnt ++;
					}
					fin.close();
				}
				else
					System.out.printf("File %s does not exist", path);
			}
		}
		catch (IOException err) {
			System.out.println("Exception: " + err);
		}
		
		if(cnt >= 1)
			System.out.println(String.format("%d accounts have been loaded.", cnt));
		else
			System.out.println("No account loaded.");
	}
	
	//Return the account if found, otherwise return null
	public Account getAccount(int number) {
		for(Account acc:accounts) {
			if(acc.getNumber() == number)
				return acc;
		}
		
		return null;
	}
	
	public boolean isExists(int number) {
		for(Account acc:accounts) {
			if(acc.getNumber() == number)
				return true;
		}
		return false;
	}

	//Save subjects' data into a text file
	public void saveAccounts() {
		String filename = "accounts.txt";
		
		try {
			//Open an output file
			Formatter fout = new Formatter(filename);
			for(Account acc:accounts) {
				acc.writeData(fout);
				fout.format("\n");
			}
			fout.close();
		}
		catch (IOException err) {
			System.out.println("Exception: " + err);
		}		
	}

	//Pop up a dialog to get input of a new account
	public void addAccount() {
		addAccountDialog = new Dialog<Account>();
		addAccountDialog.setTitle("Add a new account");
		
		addAccountPane = addAccountDialog.getDialogPane();
		addAccountPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		//Add textfields of number, bank, balance and type
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(5);
		
		TextField aNumber = new TextField();
		gp.addRow(0, new Label("Number"));
		gp.addRow(0, aNumber);
		
		TextField aBank = new TextField();
		gp.addRow(1, new Label("Bank"));
		gp.addRow(1, aBank);
		
		TextField aBalance = new TextField();
		gp.addRow(2, new Label("Balance"));
		gp.addRow(2, aBalance);
		
		TextField aType = new TextField();
		gp.addRow(3, new Label("Type"));
		gp.addRow(3, aType);
		
		addAccountPane.setContent(gp);
		
		addAccountDialog.setResultConverter((ButtonType button) -> {
			if(button == ButtonType.OK) { //OK button clicked
				Account acc = new Account();
				acc.setNumber(Integer.parseInt(aNumber.getText()));
				acc.setBank(aBank.getText());
				acc.setBalance(Double.parseDouble(aBalance.getText()));
				acc.setType(aType.getText());
				return acc;
			}
			return null;
		});

		Optional<Account> res = addAccountDialog.showAndWait();
		//An account data has been input and OK is clicked
		res.ifPresent((Account acc) -> {
			//Check if the account exists
			if(! isExists(acc.getNumber())) { //A new account
				accounts.add(acc);
				//Add to the list
				accList.getItems().add(acc.getNumber());
				messages.setText("Add an account " + acc.getNumber());
			}
			else
				messages.setText(String.format("Account %d already exists", acc.getNumber()));
		});
	}
	
	//Pop up a confirm dialog if delete a selected account
	public void deleteAccount() {
		//Get the selected item's index
		int i = accList.getSelectionModel().getSelectedIndex();
		Account acc = accounts.get(i);
		if(i < 0) { //No item selected
			messages.setText("Select an account number");
		}
		else {
			Alert confirm = new Alert(AlertType.CONFIRMATION);
			confirm.setTitle("Confirm the deletion");
			confirm.setContentText("Do you want to delete the selected account?");
			confirm.showAndWait();
			if(confirm.getResult() == ButtonType.OK) { //Confirmed
				//Remove the selected item from the list
				accList.getItems().remove(i);
				//Remove the account from the array list
				accounts.remove(i);
				messages.setText(String.format("Account %d has been removed", acc.getNumber())); 
			}
			else {
				messages.setText("The deletion is cancelled");
			}
		}
	}
	
	//Show account balance chart according to the bank
	public void showAccountsChart() {
		Dialog chartDialog = new Dialog();
		chartDialog.setTitle("Bank balance");
		
		DialogPane chartPane = chartDialog.getDialogPane();
		chartPane.getButtonTypes().add(ButtonType.OK);
		
		//Create a PieChart
		PieChart pc = new PieChart();
		
		//Calculate sum of balance according to the banks
		ArrayList<BankBalance> banks = new ArrayList<BankBalance>();
		for(Account acc:accounts) {
			boolean found = false;
			for(BankBalance b:banks) {
				if(b.equals(acc.getBank())) { //Same bank, add balance to the bank
						b.addBalance(acc.getBalance());
						found = true;
						break;
					}
			}
			if(! found) { //New bank
				BankBalance b = new BankBalance(acc.getBank(), acc.getBalance());
				banks.add(b);
			}
		}
		
		//System.out.println("There are " + banks.size() + " banks");
		
		//Create PieChart data sections and add them to the PieChart
		for(BankBalance b: banks) {
			pc.getData().add(new PieChart.Data(b.getBank(), b.getBalance()));
		}
		
		chartPane.setContent(pc);

		chartDialog.showAndWait();
	}
}
