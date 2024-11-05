// Author: Yaksh Rami 000924321
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Class for testing the Table functionality
public class TableTestProgram {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String tablename = "";
        String choice = "";
        int row = -1;
        int colNum = 1;
        String key = "";
        String s_colNum = "";
        String newValue = "";

        // Prompt user to enter the name of the tab delimited text file
        System.out.print("Enter the name of the tab delimited text file you wish to manage (e.g. codes.txt) > ");
        tablename = in.nextLine();
        Table t = new Table(tablename);
        System.out.println("Successfully loaded: " + t);

        // Display menu and handle user choices
        while (true) {
            System.out.println("\n\nTable Testing Menu\n");
            System.out.println("1. Display all data");
            System.out.println("2. Lookup");
            System.out.println("3. Search");
            System.out.println("4. Change");
            System.out.println("5. Save data to " + tablename);
            System.out.println("6. Get Single Cell Value");
            System.out.println("7. Save Single Cell Value");
            System.out.println("9. Quit");
            System.out.print("Select > ");
            choice = in.nextLine();

            switch (choice) {
                case "1":
                    // Display all data in the table
                    System.out.println("Displaying all data:\n" + t);
                    break;
                case "2":
                    // Lookup and display data at a specific row and column
                    System.out.print("Enter row number: ");
                    row = Integer.parseInt(in.nextLine());
                    System.out.print("Enter column number: ");
                    colNum = Integer.parseInt(in.nextLine());
                    System.out.println("Data at row " + row + " column " + colNum + ": " + t.getCell(row, colNum));
                    break;
                case "3":
                    // Search for a specific key in the table
                    System.out.print("Enter search key: ");
                    key = in.nextLine();
                    System.out.println("Found at row " + t.search(key));
                    break;
                case "4":
                    // Change a cell value at a specified row and column
                    System.out.print("Enter row number: ");
                    row = Integer.parseInt(in.nextLine());
                    System.out.print("Enter column number: ");
                    colNum = Integer.parseInt(in.nextLine());
                    System.out.print("Enter new value: ");
                    newValue = in.nextLine();
                    t.setCell(row, colNum, newValue);
                    System.out.println("Value changed successfully.");
                    break;
                case "5":
                    // Save data to a file
                    t.saveData();
                    System.out.println("Data saved successfully.");
                    break;
                case "6":
                    // Get and display value at a specific cell based on user input
                    System.out.print("Enter row number: ");
                    row = Integer.parseInt(in.nextLine());
                    System.out.print("Enter column number: ");
                    colNum = Integer.parseInt(in.nextLine());

                    String cellValue = t.getCell(row, colNum);

                    if (cellValue != null) {
                        System.out.println("Value at cell (" + row + ", " + colNum + "): " + cellValue);
                    } else {
                        System.out.println("Invalid cell coordinates.");
                    }

                    break;
                case "7":
                    // Save a single cell value after updating it
                    System.out.print("Enter row number: ");
                    row = Integer.parseInt(in.nextLine());
                    System.out.print("Enter column number: ");
                    colNum = Integer.parseInt(in.nextLine());
                    System.out.print("Enter new value: ");
                    newValue = in.nextLine();
                    t.setCell(row, colNum, newValue);
                    t.saveData();
                    System.out.println("Value saved successfully.");
                    break;
                case "9":
                    // Exit the program
                    System.out.println("Thank-you, good bye!");
                    System.exit(0);
                    break;
                default:
                    // Handle invalid choice selection
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
}