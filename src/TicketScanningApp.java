import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * Author: Yaksh Rami 000924321
 * This class represents a Ticket Scanning Application with a Graphical User Interface.
 * It allows scanning ticket barcodes and checks their validity based on data from a tab-delimited text file.
 * The application displays status messages indicating whether the ticket is valid, invalid, or not yet purchased.
 */
public class TicketScanningApp extends Application {
    // Instance Variables for View Components and Model
    private TextField barcodeField;
    private Button redeemButton;
    private Label statusLabel;
    private Table table;
    private Label appTitle;
    private static final String FILE_PATH = "codes.txt";
    private Set<String> redeemedTickets = new HashSet<>();

    // Inner class representing a ticket
    public static class Ticket {
        private String barcode;
        private boolean purchased;
        private boolean entered;
        private boolean redeemed;
        private String eventName;

        // Constructor
        public Ticket(String barcode, boolean purchased, boolean entered, String eventName) {
            this.barcode = barcode;
            this.purchased = purchased;
            this.entered = entered;
            this.eventName = eventName;
        }

        // Getters
        public String getBarcode() {
            return barcode;
        }

        public boolean isPurchased() {
            return purchased;
        }

        public boolean isEntered() {
            return entered;
        }

        public String getEventName() {
            return eventName;
        }

        // Method to mark a ticket as redeemed
        public void redeem() {
            redeemed = true;
            // Implement redemption logic here
            // For example, mark the ticket as redeemed
        }

        public boolean isRedeemed() {
            return redeemed;
        }
    }

    /**
     * Initializes the application's GUI components, sets up event handlers, and displays the stage.
     *
     * @param stage The main stage
     * @throws Exception if an error occurs during initialization
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Create root pane and scene
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 225);
        stage.setTitle("Ticket Scanning App");
        stage.setScene(scene);

        // Create the Table model
        table = new Table(FILE_PATH);

        // Create GUI components
        appTitle = new Label("Ticket Scanning App");
        appTitle.relocate(25, 25);
        appTitle.setFont(new Font("Arial", 40));
        appTitle.setStyle("-fx-text-fill: Blue;");

        barcodeField = new TextField();
        barcodeField.relocate(80, 100);
        barcodeField.setFont(new Font("Arial", 20));
        barcodeField.setPrefWidth(120);
        barcodeField.setPrefHeight(40);

        redeemButton = new Button("Redeem");
        redeemButton.relocate(220, 100);
        redeemButton.setFont(new Font("Arial", 20));
        redeemButton.setPrefWidth(150);
        redeemButton.setPrefHeight(40);
        redeemButton.setStyle("-fx-text-fill: blue;");
        redeemButton.setOnAction(this::handleScanButton);

        statusLabel = new Label();
        statusLabel.setLayoutX(100);
        statusLabel.setLayoutY(150);
        statusLabel.setFont(new Font("Arial", 12));

        // Add components to the root pane
        root.getChildren().addAll(barcodeField, redeemButton, statusLabel, appTitle);

        // Show the stage
        stage.show();
    }

    /**
     * Handles the button click event for redeeming a ticket.
     *
     * @param event The ActionEvent object
     */
    private void handleScanButton(ActionEvent event) {
        // Get barcode from text field
        String barcode = barcodeField.getText().trim();

        // Reset action
        if (barcode.equalsIgnoreCase("reset")) {
            resetTickets();
            statusLabel.setText("All codes have been reset.");
            statusLabel.setTextFill(Color.GREEN);
            return;
        }

        // If barcode field is empty
        if (barcode.isEmpty()) {
            playErrorSound();
            statusLabel.setText("Please enter a barcode.");
            return;
        }

        // Find ticket with the given barcode
        Ticket ticket = findTicket(barcode);

        // If ticket is not found
        if (ticket == null) {
            playErrorSound();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setFont(new Font("Arial", 16));
            statusLabel.setText("Invalid ticket: " + barcode);
            return;
        }

        // If ticket is not purchased
        if (!ticket.isPurchased()) {
            playErrorSound();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setFont(new Font("Arial", 16));
            statusLabel.setText("Ticket not purchased yet: " + barcode);
            return;
        }

        // If ticket is already redeemed
        if (ticket.isRedeemed()) {
            playErrorSound();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setFont(new Font("Arial", 16));
            statusLabel.setText("Ticket already redeemed: " + barcode);
            return;
        }

        // If ticket is a duplicate
        if (redeemedTickets.contains(barcode)) {
            playErrorSound();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setFont(new Font("Arial", 16));
            statusLabel.setText("Ticket code: " + barcode + " is a duplicate.");
            return;
        }

        // Play valid sound
        playValidsound();

        // Redeem the ticket
        ticket.redeem();
        saveRedeemedTicket(ticket.getBarcode());
        redeemedTickets.add(barcode);

        // Set status label for valid ticket
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setFont(new Font("Arial", 16));
        statusLabel.setText(ticket.getBarcode() + " - Valid\n" + ticket.getEventName());
    }

    // Method to play sound for valid ticket
    private void playValidsound() {
        String soundFile = "valid.wav"; // Path to the sound file
        Media sound = new Media(new File(soundFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    // Method to play error sound
    private void playErrorSound() {
        String errorSoundFile = "invalid.wav"; // Path to error sound file
        Media errorSound = new Media(new File(errorSoundFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(errorSound);
        mediaPlayer.play();
    }

    // Method to reset redeemed tickets
    private void resetTickets() {
        redeemedTickets.clear();
    }

    // Method to save redeemed ticket to file
    private void saveRedeemedTicket(String barcode) {
        try (FileWriter writer = new FileWriter("updatedfile.txt", true)) {
            writer.write(barcode + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for a ticket with the given barcode in the table data.
     *
     * @param barcode The barcode of the ticket to find
     * @return The Ticket object if found, null otherwise
     */
    private Ticket findTicket(String barcode) {
        String[][] grid = table.getGrid();
        for (String[] row : grid) {
            if (row.length > 0 && row[0].equals(barcode)) {
                return new Ticket(row[0], row[1].equals("Y"), row[2].equals("Y"), row[3]);
            }
        }
        return null;
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
