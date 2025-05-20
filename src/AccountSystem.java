import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;

public class AccountSystem extends Application {
    private ArrayList<Account> accounts = new ArrayList<>();

    private TextField tfNumber = new TextField();
    private TextField tfBank = new TextField();
    private TextField tfBalance = new TextField();
    private TextField tfType = new TextField();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        Label lblNumber = new Label("Account Number:");
        Label lblBank = new Label("Bank:");
        Label lblBalance = new Label("Balance:");
        Label lblType = new Label("Type:");

        Button btnAdd = new Button("Add Account");
        btnAdd.setOnAction(e -> addAccount());

        root.getChildren().addAll(
                lblNumber, tfNumber,
                lblBank, tfBank,
                lblBalance, tfBalance,
                lblType, tfType,
                btnAdd
        );

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Account System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addAccount() {
        int number = Integer.parseInt(tfNumber.getText());
        String bank = tfBank.getText();
        double balance = Double.parseDouble(tfBalance.getText());
        String type = tfType.getText();
        accounts.add(new Account(number, bank, balance, type));
        saveAccount();
    }

    public boolean setExists(int number) {
        return accounts.stream().anyMatch(a -> a.getNumber() == number);
    }

    public Account getAccount(int number) {
        return accounts.stream().filter(a -> a.getNumber() == number).findFirst().orElse(null);
    }

    public void loadAccounts() {
        try (Scanner sc = new Scanner(new File("accounts.txt"))) {
            while (sc.hasNext()) {
                Account acc = new Account();
                acc.readData(sc);
                accounts.add(acc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAccount() {
        try (Formatter f = new Formatter("accounts.txt")) {
            for (Account acc : accounts) {
                acc.writeData(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(int number) {
        accounts.removeIf(acc -> acc.getNumber() == number);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showAccountChart() {
        // Optional: Use pie charts or bar graphs from JavaFX Chart package
    }
}
