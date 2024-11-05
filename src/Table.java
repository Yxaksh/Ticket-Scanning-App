import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

// Class representing a Table with grid data
//Author: Yaksh Rami 000924321
public class Table {
    private String tablename; // Name of the table
    private int numRows; // Number of rows in the table
    private int numCols; // Number of columns in the table
    private String[][] grid; // 2D array to store table data

    // Constructor to initialize the Table object with data from a file
    public Table(String filename) {
        tablename = filename;
        numRows = 0;
        numCols = 0;
        String s;
        int r;
        String[] item;

        try {
            // Read data from file to determine dimensions of the table
            Scanner theFile = new Scanner(new FileInputStream(new File(tablename)));
            while (theFile.hasNextLine()) {
                s = theFile.nextLine();
                item = s.split("\t", 0);

                if (item.length > numCols)
                    numCols = item.length;

                numRows++;
            }
            theFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Table class Error 1: file not found.");
        }

        grid = new String[numRows][numCols]; // Initialize grid with determined dimensions

        try {
            // Read data from file and populate the grid
            Scanner theFile = new Scanner(new FileInputStream(new File(tablename)));
            r = 0;
            while (theFile.hasNextLine()) {
                s = theFile.nextLine();
                item = s.split("\t", 0);

                for (int c = 0; c < numCols; c++) {
                    if (item[c].length() == 0)
                        grid[r][c] = "";
                    else
                        grid[r][c] = item[c];
                }
                r++;
            }
            theFile.close();
        } catch (Exception e) {
            System.out.println("Table class error 2: file not found.");
        }
    }

    // Get method to retrieve the grid data
    public String[][] getGrid() {
        return grid;
    }

    // Method to find a Ticket object by barcode in the grid
    public Ticket findTicket(String barcode) {
        for (String[] row : grid) {
            if (row.length > 0 && row[0].equals(barcode)) {
                return new Ticket(row[0], row[1].equals("Y"), row[2].equals("Y"), row[3]);
            }
        }
        return null;
    }

    // Inner class representing a Ticket object
    public class Ticket {
        private String barcode;
        private boolean purchased;
        private boolean entered;
        private String eventName;

        // Constructor for Ticket object
        public Ticket(String barcode, boolean purchased, boolean entered, String eventName) {
            this.barcode = barcode;
            this.purchased = purchased;
            this.entered = entered;
            this.eventName = eventName;
        }

        // Getter methods for Ticket attributes
        public String getBarcode() { return barcode; }
        public boolean isPurchased() { return purchased; }
        public boolean isEntered() { return entered; }
        public String getEventName() { return eventName; }

        // Method to handle ticket redemption logic
        public void redeem() {
            // Implement redemption logic here
            // For example, mark the ticket as redeemed
        }

        // Method to check if a ticket is redeemed
        public boolean isRedeemed() { return false; }
    }

    // Method to get cell value at specified row and column indices
    public String getCell(int row, int col) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            return grid[row][col];
        } else {
            return null;
        }
    }

    // Method to search for a specific value in the grid
    public boolean search(String value) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to set cell value at specified row and column indices
    public void setCell(int row, int col, String value) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            grid[row][col] = value;
        } else {
            System.out.println("Invalid row or column index.");
        }
    }

    // Method to save table data to a file with specified filename
    public void saveData(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    writer.write(grid[i][j]);
                    if (j < numCols - 1) {
                        writer.write("\t");
                    }
                }
                writer.write("\n");
            }
            System.out.println("Data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Overloaded method to save data using a default filename
    public void saveData() { saveData("updatedcodes.txt"); }

    // Method to provide a string representation of the Table object
    public String toString() {
        return "Table: " + tablename + ", rows: " + numRows + ", columns: " + numCols;
    }
}