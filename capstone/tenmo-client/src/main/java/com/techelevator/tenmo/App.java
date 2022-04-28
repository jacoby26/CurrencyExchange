package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private ServiceBase serviceBase;

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        else {
            ServiceBase.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        AccountService view = new AccountService("http://localhost:8080/account/");
        BigDecimal balance = view.getBalance(currentUser);
        System.out.println("Your current account balance is: $" + balance);
        //make UI option ^^
    }

	private void viewTransferHistory() {
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        List<Transfer> transfers = transferService.getHistory(currentUser);
        Boolean isInList = false;
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID \t \t \t From/To \t \t \t Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            if (transfer.getTransferTypeId() == 1) {
                System.out.println(transfer.getTransferId() + " \t \t From: " + transfer.getAccountFrom() + " \t \t $ " + transfer.getAmount());
            } else {
                System.out.println(transfer.getTransferId() + "\t \t To: " + transfer.getAccountTo() + " \t \t \t $ " + transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
        long input = (long) consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        //make UI option ^^
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == input) {
                isInList = true;
                break;
            }
        }
        if (transferService.getTransfer(input) != null && isInList)
        {
            viewTransferDetails(input);
        } else {
            System.out.println("Not a valid Transfer Id");
        }
    }

    private void viewTransferDetails(Long transferId)
    {
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        Transfer transfer = transferService.getTransfer(transferId);
        System.out.println(transfer.toString());
    }


	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        transferService.makeTransfer(2L,2L,2001L, 2002L, new BigDecimal("25.00"));
	}

	private void requestBucks() {
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        transferService.makeTransfer(1L,1L,2003L, 2001L, new BigDecimal("25.00"));
	}

}
