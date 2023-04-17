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
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.TranslationsString;
import utilities.GlobalVariables;

/** The class containing the Student Categorization and Reflection Choice View for the Gen Ed Assessment Data
 *  Management application
 */
//==============================================================
public class StudentCategorizationAndReflectionChoiceView extends View {
    // GUI components
    protected Button addNewSCButton;
    protected Button addNewIRButton;
    protected Button cancelButton;

    protected TranslationsString ts = new TranslationsString();

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public StudentCategorizationAndReflectionChoiceView(IModel sgeat)
    {
        // sgeat - Search Gen Ed Area Transaction (any controller
        // that requires Gen Ed Areas to be searched for)
        super(sgeat, "SearchGenEdAreaView");


        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: slategrey");
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        container.getChildren().add(createCopyrightPanel());

        getChildren().add(container);

        populateFields();


        myModel.subscribe("TransactionError", this);
    }


    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel()
    {
        return new CopyrightPanel();
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        return new CommonTitleWithoutLogoPanel();

    }

    //-------------------------------------------------------------
    protected String getActionText()
    {
        return "";
        //"(in full if known, or in part):";
    }

    // Create the main form content
    //-------------------------------------------------------------
    public VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        Text blankText = new Text("  ");
        blankText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText.setWrappingWidth(350);
        blankText.setTextAlignment(TextAlignment.CENTER);
        blankText.setFill(Color.WHITE);
        vbox.getChildren().add(blankText);


        addNewSCButton = new Button(getSCButtonText());
        addNewSCButton.setStyle("-fx-focus-color: darkgreen;");
        addNewSCButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("AddNewStudentCategorization", null);
        });
        vbox.getChildren().add(addNewSCButton);
        Text blankText1 = new Text("  ");
        blankText1.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText1.setWrappingWidth(350);
        blankText1.setTextAlignment(TextAlignment.CENTER);
        blankText1.setFill(Color.WHITE);
        vbox.getChildren().add(blankText1);

        addNewIRButton = new Button(getIRButtonText());
        addNewIRButton.setStyle("-fx-focus-color: darkgreen;");
        addNewIRButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("AddNewInstructorReflection", null);
        });
        vbox.getChildren().add(addNewIRButton);
        Text blankText2 = new Text("  ");
        blankText2.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText2.setWrappingWidth(350);
        blankText2.setTextAlignment(TextAlignment.CENTER);
        blankText2.setFill(Color.WHITE);
        vbox.getChildren().add(blankText2);





        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
        });
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
        });



        ImageView icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_Return");
        }
        catch (Exception ex) {

        }
        cancelButton = new Button(ts.getDisplayString(),icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelSearchArea", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(doneCont);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    //-------------------------------------------------------------
    protected String getSCButtonText(){
        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_AddNewStudentCategorizations");
        }
        catch (Exception ex) {

        }
        return ts.getDisplayString();
    }

    //-------------------------------------------------------------
    protected String getIRButtonText(){
        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_AddNewInstructorReflections");
        }
        catch (Exception ex) {

        }
        return ts.getDisplayString();
    }


    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        clearValues();
        clearErrorMessage();
    }

    //----------------------------------------------------------
    public void clearValues()
    {

    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearValues();
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}

//---------------------------------------------------------------
//	Revision History:
//