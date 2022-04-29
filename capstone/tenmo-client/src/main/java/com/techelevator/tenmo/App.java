package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

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
        AccountService accountService = new AccountService(("http://localhost:8080/account/"));
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        Transfer transfer = transferService.getTransfer(transferId);
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + accountService.getUsername(transfer.getAccountFrom()));
        System.out.println("To: " + accountService.getUsername(transfer.getAccountTo()));
        System.out.println("Type: " + transfer.transferType());
        System.out.println("Status: " + transfer.transferStatus());
        System.out.println("Amount: $" + transfer.getAmount());
    }


	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {

        UserService userService = new UserService("http://localhost:8080/user/");
        AccountService accountService = new AccountService("http://localhost:8080/account/");
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");

        List<String> usernames = userService.getAllUsernames();

        System.out.println("-------------------------------------------");
        System.out.println("Make a transfer");
        String usernameInput = consoleService.promptForString("Username of recipient (enter (s)how for available users): ");

        if(usernameInput.toLowerCase().equals("s")) //display list of all users but logged in user
        {
            System.out.println("-------------------------------------------");
            System.out.println("\nList of available users:");
            for (String username : usernames)
            {
                if (!username.equals(usernameInput)) // need to exclude current user
                {
                    System.out.println(username);
                }
            }
            System.out.println("");
            sendBucks();
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Amount to send in dollars: ");
        System.out.println("-------------------------------------------");

        Long accountTo = userService.getAccountId(usernameInput);
        Long accountFrom = accountService.getAccountId(currentUser);
        transferService.makeTransfer(2L,2L, accountFrom, accountTo, amount);
	}

	private void requestBucks() {
        TransferService transferService = new TransferService("http://localhost:8080/transfer/");
        transferService.makeTransfer(1L,1L,2003L, 2001L, new BigDecimal("25.00"));
	}

}
