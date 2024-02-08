// tabs=4
//************************************************************
//	COPYRIGHT 2023, Ethan L. Baker, Matthew E. Morgan and
//  Sandeep Mitra, State University of New York. - Brockport
//  (SUNY Brockport)
//	ALL RIGHTS RESERVED
//
// This file is the product of SUNY Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of SUNY Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;
import model.Transaction;

import javax.swing.*;

/**
 * The class containing the Main View for the Gen Ed Assessment Data Management application
 */
//==============================================================
public class AddSemesterView extends View {

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;
    protected final String[] allowedSemesters = {"Fall", "Spring", "Summer", "Winter"};


    // GUI components
    protected Button submit;
    protected ComboBox semester;
    protected TextField year;

    // other GUI Components here

    protected Button cancelButton;
    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    protected String keyToSendWithData = "";

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddSemesterView(IModel Semester) {

        super(Semester, "AddSemesterView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setStyle("-fx-background-color: slategrey");

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // how do you add white space?
        //container.getChildren().add(new Label(" "));

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());

        container.getChildren().add(createStatusLog("             "));

        container.getChildren().add(createCopyrightPanel());

        // container.setMinHeight(550);
        //container.setMinWidth(550);

        getChildren().add(container);

        populateFields();
        cancelButton.requestFocus();

	   keyToSendWithData = "SemesterData";
        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {

        return new CommonTitleWithoutLogoPanel();
    }

  

   //------------------------------------------------------------
    protected VBox createFormContents() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("Enter the Semester's Information");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text semesterLabel = new Text(" Semester : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        semesterLabel.setFont(myFont);
        semesterLabel.setWrappingWidth(150);
        semesterLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(semesterLabel, 0, 1);
        grid.add(semester = new ComboBox(FXCollections.observableArrayList(allowedSemesters)), 1, 1);

        Text yearLabel = new Text(" Year : ");
        yearLabel.setFont(myFont);
        yearLabel.setWrappingWidth(150);
        yearLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(yearLabel, 0, 2);
        grid.add(year = new TextField(), 1, 2);


        ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submit = new Button("Add", icon);
        submit.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submit.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();
            String semesterName = (String) semester.getValue();
            String enteredYear = year.getText();
            if ((enteredYear != null) && (enteredYear.length() > 0)) {
                try {
                    if (Integer.parseInt(enteredYear) >= 2000 && Integer.parseInt(enteredYear) <= 2100) {
                        props.setProperty("SemName", semesterName);
                        props.setProperty("Year", enteredYear);
                        myModel.stateChangeRequest(keyToSendWithData, props);
                    } else {
                        year.setStyle("-fx-border-color: firebrick;");
                        displayErrorMessage("ERROR: Please enter a year value between 2000 and 2100!");
                    }
                } catch (NumberFormatException excep) {
                    displayErrorMessage("ERROR: Please enter a valid year value (2000-2100)!");
                }
            } else {
                year.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a valid year value (2000-2100)!");
            }
        });

        submit.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submit.setEffect(new DropShadow());
        });
        submit.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submit.setEffect(null);
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);


        buttonContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            buttonContainer.setStyle("-fx-background-color: GOLD");
        });
        buttonContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            buttonContainer.setStyle("-fx-background-color: SLATEGREY");
        });
        icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        cancelButton = new Button("Return", icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        buttonContainer.getChildren().add(submit);
        buttonContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel() {
        return new CopyrightPanel();
    }

    //-------------------------------------------------------------
    public void populateFields() {
        semester.getSelectionModel().select(0);
    }
   //-------------------------------------------------------
    protected void clearOutlines() {
        semester.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
        year.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if (key.equals("TransactionError") == true) {
            // display the passed text
            String message = (String) value;
            if ((message.startsWith("Err")) || (message.startsWith("ERR"))) {
                displayErrorMessage(message);
            } else {
                displayMessage(message);
            }

        }
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display Informational message
     */
    //----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }
}






