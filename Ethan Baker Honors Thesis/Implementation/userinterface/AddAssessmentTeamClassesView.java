// tabs=4
//************************************************************
//	COPYRIGHT 2022, Ethan L. Baker, Matthew E. Morgan and
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Properties;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;


// project imports
import impresario.IModel;

/**
 * The class containing the AddAssessmentTeamClassesView for the Gen Ed Area Data Management application
 */
//==============================================================
public class AddAssessmentTeamClassesView extends View {

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;


    // GUI components
    protected Button submitButton;
    protected TextField crsCode;
    protected TextField number;
    protected Text genEdArea;
    protected Text sem;

    // other GUI Components here

    protected Button cancelButton;
    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    protected String keyToSendWithData = "";

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddAssessmentTeamClassesView(IModel AssessmentTeamClasses) {

        super(AssessmentTeamClasses, "AddAssessmentTeamClassesView");

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
        container.getChildren().add(new Text("          ")); // Blank space to handle scroll down


        // container.setMinHeight(550);
        //container.setMinWidth(550);

        getChildren().add(container);

        populateFields();
        cancelButton.requestFocus();

        keyToSendWithData = "AssessmentTeamClassesData";
        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {

        return new CommonTitleWithoutLogoPanel();
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Add course code and course number for:";
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
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text genEdLabel = new Text(" Gen Ed Area : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        genEdLabel.setFont(myFont);
        genEdLabel.setWrappingWidth(150);
        genEdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genEdLabel, 0, 1);
        grid.add(genEdArea = new Text(), 1, 1);

        Text semLabel = new Text(" Semester : ");
        semLabel.setFont(myFont);
        semLabel.setWrappingWidth(150);
        semLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(semLabel, 0, 2);
        grid.add(sem = new Text(), 1, 2);

        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(10, 10, 10, 10));

        Text courseCode = new Text(" Course Code : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        courseCode.setFont(myFont);
        courseCode.setWrappingWidth(150);
        courseCode.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(courseCode, 0,0);
        grid2.add( crsCode = new TextField(),1 , 0);
        crsCode.setPrefWidth(80);
        crsCode.setMaxWidth(80);

        Text crsNum = new Text(" Number : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        crsNum.setFont(myFont);
        crsNum.setWrappingWidth(150);
        crsNum.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(crsNum, 0,1);
        grid2.add(number = new TextField(), 1,1);
        number.setPrefWidth(80);
        number.setMaxWidth(80);


        ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Add", icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();

            String curseCode = (String) crsCode.getText();
            String courseNumber = number.getText();

            try {
                if (!(curseCode.length() > 0))
                {
                    crsCode.setStyle("-fx-border-color: firebrick;");
                    displayErrorMessage("ERROR: Please enter a proper course code");
                }
                else
                {
                    Properties props = new Properties();
                    props.setProperty("CourseDisciplineCode", curseCode);
                    props.setProperty("CourseNumber", courseNumber);
                    myModel.stateChangeRequest(keyToSendWithData, props);
                }
            }
            catch (Exception ex)
            {
                number.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a proper course number");
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
                myModel.stateChangeRequest("CancelAddAssessmentTeamClasses", null);
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
        vbox.getChildren().add(grid2);
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
        String genEd = (String)myModel.getState("GenEdAreaData");
        String semester = (String)myModel.getState("SemData");
        if (genEd != null)
        {
            genEdArea.setText(genEd);
        }
        if (semester != null)
        {
            sem.setText(semester);
        }
    }
    //-------------------------------------------------------
    protected void clearOutlines() {
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






