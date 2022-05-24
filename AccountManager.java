import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.io.BufferedReader;

public class AccountManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        //Creating an instance of our user accounts class to handle user information and log in credentials
        AllUserAccounts userAccounts = new AllUserAccounts();
        
        boolean Open = true;
        do
        {
            //Display the main menu and list it's options. "login", "new", and "exit"
            System.out.println("Welcome to Johnson's Account Manager!\nType any of these options to continue:");
            System.out.println("1. login- log in to your account\n2. new - create a new account\n3.exit - leave the account manager\n");
            System.out.println(">>> ");
            String input = scanner.next(); //Get the user's input for the main menu
             
            //Begin opening different sections of the manager according to the user's input
            switch(input) {
                //New account
                case "new":
                    Boolean creatingAccount = true;
                    String newUsername;
                    String newPassword;
                    do {
                        System.out.println("Let's create your account!");
                        System.out.println("Username:");
                        newUsername = scanner.next();
                        System.out.println("Password: ");
                        newPassword = scanner.next();
                        
                        //Checking that the username that has been provided does not already exist
                        if(!userAccounts.accountsDictionary.containsKey(newUsername)) {
                            System.out.println("What's your first name?");
                            String newFirstName = scanner.next();
                            System.out.println("What's your last name?");
                            String newLastName = scanner.next();
                            System.out.println("What's your birthday? mm/dd/yy");
                            String newBirthday = scanner.next();

                            userAccounts.addAccountToFile(newUsername,newPassword,newFirstName, newLastName, newBirthday);
                            break;
                            
                        } else {
                            System.out.println("Sorry, that username already exists");
                            Boolean exitQuestion = true;
                            while(exitQuestion) {
                                System.out.println("Would you like to try again?");
                                System.out.println("y/n");
                                String exitYesOrNo = scanner.next();
                                if(exitYesOrNo.equals("y")) {
                                    exitQuestion = false;
                                } else if(exitYesOrNo.equals("n")){
                                    creatingAccount = false;
                                    exitQuestion = false;
                                    break;
                                }
                                else{
                                    System.out.println("Sorry, try typing y or n");
                                }
                            }
                        }   
                    } while(creatingAccount);
                    break;
                    
                //Logging into an existing account
                case "login":
                    Boolean attempting = true;
                    while(attempting){
                        System.out.println("\nLog in:");
                        System.out.println("Username");
                        String usernameEntry = scanner.next();
                        System.out.println("Password");
                        String passwordEntry = scanner.next();
                        if(userAccounts.accountsDictionary.containsKey(usernameEntry)){
                            if (userAccounts.accountsDictionary.get(usernameEntry)[0].equals(passwordEntry)){
                                //Enterring the logged in menu
                                Boolean loggedIn = true;
                                do {
                                    System.out.println("\nWelcome," + userAccounts.accountsDictionary.get(usernameEntry)[1]);
                                    System.out.println("1. info");
                                    System.out.println("2. exit");
                                    String loggedInEntry = scanner.next();
                                    switch(loggedInEntry){
                                        //Displaying all available information for the account
                                        case "info":
                                            String[] UserInfo = userAccounts.accountsDictionary.get(usernameEntry);
                                            System.out.println("\nUsername: " + usernameEntry);
                                            System.out.println("First Name: " + UserInfo[1]);
                                            System.out.println("Last Name: " + UserInfo[2]);
                                            System.out.println("Birthday: "+ UserInfo[3]);
                                            break;

                                        case "exit":
                                            loggedIn = false;
                                            attempting = false;
                                            break;
                                    }
                                } while(loggedIn);
                            
                        }else{
                            boolean askingExit = true;
                            while(askingExit) {
                                System.out.println("Sorry, that password is incorrect. Would you like to try again or return to the main menu? y/n");
                                String exitResponse = scanner.next();
                                if(exitResponse.equals("n")){
                                    attempting = false;
                                    askingExit = false;
                                }else if(exitResponse.equals("y")){
                                    askingExit = false;
                                }else{
                                    System.out.println("Sorry, try typing either y or n");
                                }
                            }
                    }
                
                    } else {
                        Boolean askingExit = true;
                        while(askingExit) {
                            System.out.println("\nSorry, that username doesn't exist. would you like to try again or return to the main menu? y/n");
                            String exitResponse = scanner.next();  
                            if(exitResponse.equals("n")) {
                                attempting = false;
                                askingExit = false;
                            }else if(exitResponse.equals("y")) {

                                askingExit = false;

                            }else{
                                System.out.println("Sorry, try typing either y or n");
                            }
                        }
                    }
                    }
                    break;

                //Leaving the main menu itself
                case "exit":
                    System.out.println("Exit");
                    Open = false;
                    break;
        } 

        } while(Open);
            //After exiting the main menu
            System.out.println("Ok bye!");
            scanner.close();
    }
}

//Class that handles retreiving and storing the users' information
class AllUserAccounts{
    Hashtable<String,String[]> accountsDictionary;
    //Constructor for the class
    AllUserAccounts() {
        File accountsFile = new File(System.getProperty("user.dir") + "/accounts.txt");
        BufferedReader accountFileReader;
        accountsDictionary = new Hashtable<String,String[]>();

        try {
            //Creating the text file if it hasn't been created already, to save the users' information
            if (accountsFile.createNewFile()) {
                System.out.println("Save File Created");
            } 
            accountFileReader = new BufferedReader((new FileReader(System.getProperty("user.dir") + "/accounts.txt")));
            String new_line = accountFileReader.readLine();
            while(new_line != null) {
                
                String[] split_line = new_line.split(":");
                accountsDictionary.put(split_line[0],split_line[1].split(","));
                new_line = accountFileReader.readLine();
            }

        } catch(IOException error) {
            System.out.println("Error");
        }
    }
    //Method to store a new account in the current dictionary, and permanently in the file that holds all user information
    void addAccountToFile(String username,String password,String firstName,String lastName,String birthday){
        String accountString = username + ":" + password + "," + firstName + "," + lastName + "," + birthday + ",";
        String[] accountInfo = {password,firstName ,lastName,birthday};
        try {
                FileWriter newAccountWriter = new FileWriter(System.getProperty("user.dir") + "/accounts.txt",true);
                accountsDictionary.put(username,accountInfo);
                newAccountWriter.write(accountString + "\n");
                newAccountWriter.close();
            } catch(IOException error) {
                System.out.println("Error");
            }
    }
}





