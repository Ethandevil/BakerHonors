package userinterface;

// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams and Sandeep Mitra, State
//   University of New York. - Brockport (SUNY Brockport)
//	ALL RIGHTS RESERVED
//
// This file is the product of SUNY Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of SUNY Brockport.
//************************************************************
//


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
 * The class containing the Add Performance Category View for the ISLO Data Management application
 */
//==============================================================
public class AddPerformanceCategoryView extends View {

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;


    // GUI components
    protected Button submitButton;
    protected TextField number;
    protected TextField name;

    // other GUI Components here

    protected Button cancelButton;
    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    protected String keyToSendWithData = "";

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddPerformanceCategoryView(IModel PerformanceCategory) {

        super(PerformanceCategory, "AddPerformanceCategoryView");

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

        container.getChildren().add(createStatusLog("                               "));

        container.getChildren().add(createCopyrightPanel());

        // container.setMinHeight(550);
        //container.setMinWidth(550);

        getChildren().add(container);

        populateFields();
        cancelButton.requestFocus();

        keyToSendWithData = "PerformanceCategoryData";
        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {

        return new CommonTitleWithoutLogoPanel();
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Enter the Performance Category Information";
    }
    //------------------------------------------------------------
    protected VBox createFormContents() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text(getPromptText());
        prompt.setWrappingWidth(400);
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text numberLabel = new Text(" Number : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        numberLabel.setFont(myFont);
        numberLabel.setWrappingWidth(150);
        numberLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(numberLabel, 0, 1);
        grid.add(number = new TextField(), 1, 1);

        Text nameLabel = new Text(" Name : ");
        nameLabel.setFont(myFont);
        nameLabel.setWrappingWidth(150);
        nameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(nameLabel, 0, 2);
        grid.add(name = new TextField(), 1, 2);


        ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Add", icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();
            String categoryName = (String) name.getText();
            String categoryNum = number.getText();

            if (categoryNum.length() > 0 && categoryNum.matches("[0-9]+"))
            {
                props.setProperty("Number", categoryNum);
                if (categoryName.length() > 0 && categoryName.matches("[a-zA-Z0-9-() ]+")) {

                    props.setProperty("Name", categoryName);
                    clearData();
                    myModel.stateChangeRequest("PerformanceCategoryData", props);
                }
                else
                {
                    name.setStyle("-fx-border-color: firebrick;");
                    displayErrorMessage("ERROR: Please enter a valid category name!");
                }

            }
            else
            {
                number.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a valid category number!");
            }
        });

        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submitButton.setEffect(new DropShadow());
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submitButton.setEffect(null);
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
                myModel.stateChangeRequest("CancelAddPerformanceCategory", null);
            }
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        buttonContainer.getChildren().add(submitButton);
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
    }
    //-------------------------------------------------------
    protected void clearOutlines() {
        name.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
        number.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
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
     * Clear GUI Values
     */
    //----------------------------------------------------------
    public void clearData() {
        name.setText("");
        number.setText("");

    }
    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();

    }
}






